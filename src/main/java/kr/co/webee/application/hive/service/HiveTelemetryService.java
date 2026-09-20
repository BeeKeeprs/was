package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveTelemetryRequest;
import kr.co.webee.application.hive.dto.response.HiveTelemetrySseResponse;
import kr.co.webee.application.hive.dto.response.HiveTelemetryResponse;
import kr.co.webee.application.hive.dto.response.HiveTelemetryResponse.DataPoint;
import kr.co.webee.application.hive.dto.response.HiveTelemetryResponse.HwIssue;
import kr.co.webee.application.sse.service.SseEmitterService;
import kr.co.webee.application.sse.type.SseEventType;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveTelemetry;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.repository.HiveTelemetryRepository;
import kr.co.webee.domain.hive.type.Interval;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.domain.hive.type.SensorType;
import kr.co.webee.domain.hive.type.SlotStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HiveTelemetryService {
    private final HiveRepository hiveRepository;
    private final HiveTelemetryRepository hiveTelemetryRepository;
    private final SseEmitterService sseEmitterService;

    @Transactional
    public void recordTelemetry(HiveTelemetryRequest request, String macAddress) {
        Hive hive = hiveRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 벌통입니다. macAddress=" + macAddress));

        HiveTelemetry telemetry = request.toEntity(hive);
        hiveTelemetryRepository.save(telemetry);

        sseEmitterService.sendToClient(SseEventType.HIVE_TELEMETRY, hive.getUser().getId(),
                HiveTelemetrySseResponse.from(telemetry));
    }

    @Transactional(readOnly = true)
    public HiveTelemetryResponse getTelemetry(Long hiveId, Long userId, Period period, SensorType sensorType,
                                               LocalDateTime from, Interval interval) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        LocalDateTime start;
        LocalDateTime end;
        SlotStrategy strategy;

        if (period == Period.HOUR) {
            if (from == null || interval == null) {
                throw new BusinessException(ErrorType.HIVE_TELEMETRY_INVALID_QUERY);
            }
            start = from.truncatedTo(ChronoUnit.HOURS);
            end = start.plusHours(1);
            strategy = interval;
        } else {
            end = LocalDateTime.now();
            start = period.startFrom(end);
            strategy = period;
        }

        List<HiveTelemetry> telemetries = hiveTelemetryRepository.findByHiveIdAndRecordedAtBetween(hiveId, start, end);

        Map<LocalDateTime, List<Double>> grouped = groupBySlot(sensorType, telemetries, strategy);
        Map<LocalDateTime, List<HwIssue>> issuesGrouped = groupIssuesBySlot(telemetries, strategy);

        List<DataPoint> data = generateSlots(start, end, strategy).stream()
                .map(slot -> DataPoint.of(
                        strategy.formatLabel(slot),
                        calculateAverage(slot, grouped),
                        issuesGrouped.getOrDefault(slot, List.of())
                ))
                .toList();

        return HiveTelemetryResponse.of(sensorType, period, data);
    }

    private Map<LocalDateTime, List<HwIssue>> groupIssuesBySlot(List<HiveTelemetry> telemetries, SlotStrategy strategy) {
        return telemetries.stream()
                .filter(t -> t.getHwIssue() != null)
                .collect(Collectors.groupingBy(
                        t -> strategy.truncate(t.getRecordedAt()),
                        Collectors.mapping(
                                t -> new HwIssue(t.getHwIssue(), t.getHwIssueTimestamp()),
                                Collectors.toList()
                        )
                ));
    }

    private Map<LocalDateTime, List<Double>> groupBySlot(SensorType sensorType, List<HiveTelemetry> telemetries,
                                                          SlotStrategy strategy) {
        return telemetries.stream()
                .collect(Collectors.groupingBy(
                        t -> strategy.truncate(t.getRecordedAt()),
                        Collectors.mapping(sensorType::extract, Collectors.toList())
                ));
    }

    private List<LocalDateTime> generateSlots(LocalDateTime start, LocalDateTime end, SlotStrategy strategy) {
        List<LocalDateTime> slots = new ArrayList<>();

        LocalDateTime current = strategy.truncate(start);

        while (!current.isAfter(end)) {
            slots.add(current);
            current = strategy.next(current);
        }
        return slots;
    }

    private Double calculateAverage(LocalDateTime slot, Map<LocalDateTime, List<Double>> grouped) {
        List<Double> values = grouped.getOrDefault(slot, List.of()).stream()
                .filter(Objects::nonNull)
                .toList();

        return values.isEmpty() ? null
                : values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
