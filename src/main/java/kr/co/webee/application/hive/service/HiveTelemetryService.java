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
import java.util.function.Function;
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
    public HiveTelemetryResponse getTelemetry(Long hiveId, Long userId, Period period,
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

        Map<LocalDateTime, List<HiveTelemetry>> grouped = telemetries.stream()
                .collect(Collectors.groupingBy(t -> strategy.truncate(t.getRecordedAt())));

        List<DataPoint> data = generateSlots(start, end, strategy).stream()
                .map(slot -> aggregateSlot(strategy.formatLabel(slot), grouped.getOrDefault(slot, List.of())))
                .toList();

        return HiveTelemetryResponse.of(period, data);
    }

    private DataPoint aggregateSlot(String label, List<HiveTelemetry> slotData) {
        if (slotData.isEmpty()) {
            return DataPoint.builder().label(label).issues(List.of()).build();
        }

        HiveTelemetry last = slotData.get(slotData.size() - 1);

        List<HwIssue> issues = slotData.stream()
                .filter(t -> t.getHwIssue() != null)
                .map(t -> new HwIssue(t.getHwIssue(), t.getHwIssueTimestamp()))
                .toList();

        return DataPoint.builder()
                .label(label)
                .internalTemperature(average(slotData, HiveTelemetry::getInternalTemperature))
                .externalTemperature(average(slotData, HiveTelemetry::getExternalTemperature))
                .internalHumidity(average(slotData, HiveTelemetry::getInternalHumidity))
                .externalHumidity(average(slotData, HiveTelemetry::getExternalHumidity))
                .co2(average(slotData, HiveTelemetry::getCo2))
                .peltierMode(last.getPeltierMode())
                .peltierDutyPct(average(slotData, HiveTelemetry::getPeltierDutyPct))
                .fanHotDutyPct(average(slotData, HiveTelemetry::getFanHotDutyPct))
                .fanColdDutyPct(average(slotData, HiveTelemetry::getFanColdDutyPct))
                .fanState(last.getFanState())
                .targetTemperature(average(slotData, HiveTelemetry::getTargetTemperature))
                .internalSensorValid(last.getInternalSensorValid())
                .externalSensorValid(last.getExternalSensorValid())
                .peltierCoolCurrentA(average(slotData, HiveTelemetry::getPeltierCoolCurrentA))
                .peltierHeatCurrentA(average(slotData, HiveTelemetry::getPeltierHeatCurrentA))
                .issues(issues)
                .build();
    }

    private Double average(List<HiveTelemetry> slotData, Function<HiveTelemetry, Double> extractor) {
        List<Double> values = slotData.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .toList();
        return values.isEmpty() ? null : values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
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
}
