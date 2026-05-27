package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveTelemetryRequest;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveTelemetry;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.repository.HiveTelemetryRepository;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.domain.hive.type.SensorType;
import kr.co.webee.application.hive.dto.response.HiveTelemetryResponse;
import kr.co.webee.application.hive.dto.response.HiveTelemetryResponse.DataPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    public void recordTelemetry(HiveTelemetryRequest request, String macAddress) {
        Hive hive = hiveRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 벌통입니다. macAddress=" + macAddress));

        HiveTelemetry telemetry = request.toEntity(hive);

        hiveTelemetryRepository.save(telemetry);
    }

    @Transactional(readOnly = true)
    public HiveTelemetryResponse getTelemetry(Long hiveId, Long userId, Period period, SensorType sensorType) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = period.startFrom(now);

        List<HiveTelemetry> telemetries = hiveTelemetryRepository.findByHiveIdAndRecordedAtBetween(hiveId, start, now);

        Map<LocalDateTime, List<Double>> grouped = groupSensorValuesBySlot(period, sensorType, telemetries);

        List<DataPoint> data = generateSlots(start, now, period).stream()
                .map(slot -> DataPoint.of(
                        period.formatLabel(slot),
                        calculateAverage(slot, grouped)
                ))
                .toList();

        return HiveTelemetryResponse.of(sensorType, period, data);
    }

    private Map<LocalDateTime, List<Double>> groupSensorValuesBySlot(Period period, SensorType sensorType, List<HiveTelemetry> telemetries) {
        return telemetries.stream()
                .collect(Collectors.groupingBy(
                        t -> period.truncate(t.getRecordedAt()),
                        Collectors.mapping(sensorType::extract, Collectors.toList())
                ));
    }

    private List<LocalDateTime> generateSlots(LocalDateTime start, LocalDateTime end, Period period) {
        List<LocalDateTime> slots = new ArrayList<>();

        LocalDateTime current = period.truncate(start);

        while (!current.isAfter(end)) {
            slots.add(current);
            current = period.next(current);
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
