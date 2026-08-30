package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateTelemetryRequest;
import kr.co.webee.application.gate.dto.response.GateTelemetryResponse;
import kr.co.webee.application.gate.dto.response.GateTelemetryResponse.DataPoint;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.entity.GateTelemetry;
import kr.co.webee.domain.gate.repository.GateRepository;
import kr.co.webee.domain.gate.repository.GateTelemetryRepository;
import kr.co.webee.domain.gate.type.GateSensorType;
import kr.co.webee.domain.hive.type.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GateTelemetryService {
    private final GateRepository gateRepository;
    private final GateTelemetryRepository gateTelemetryRepository;

    @Transactional
    public void recordTelemetry(GateTelemetryRequest request, String macAddress) {
        Gate gate = gateRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 개폐기입니다. macAddress=" + macAddress));

        gateTelemetryRepository.save(request.toEntity(gate));
    }

    @Transactional(readOnly = true)
    public GateTelemetryResponse getTelemetry(Long gateId, Long userId, Period period, GateSensorType sensorType) {
        gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = period.startFrom(now);

        List<GateTelemetry> telemetries = gateTelemetryRepository.findByGateIdAndRecordedAtBetween(gateId, start, now);

        Map<LocalDateTime, List<Double>> grouped = telemetries.stream()
                .collect(Collectors.groupingBy(
                        t -> period.truncate(t.getRecordedAt()),
                        Collectors.mapping(sensorType::extract, Collectors.toList())
                ));

        List<DataPoint> data = generateSlots(start, now, period).stream()
                .map(slot -> DataPoint.of(
                        period.formatLabel(slot),
                        calculateAverage(slot, grouped)
                ))
                .toList();

        return GateTelemetryResponse.of(sensorType, period, data);
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
