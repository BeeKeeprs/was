package kr.co.webee.application.gate.dto.request;

import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.entity.GateTelemetry;

import java.time.LocalDateTime;

public record GateTelemetryRequest(
        Double temperature,
        Double humidity,
        LocalDateTime timestamp
) {
    public GateTelemetry toEntity(Gate gate) {
        return GateTelemetry.builder()
                .temperature(temperature)
                .humidity(humidity)
                .recordedAt(timestamp)
                .gate(gate)
                .build();
    }
}
