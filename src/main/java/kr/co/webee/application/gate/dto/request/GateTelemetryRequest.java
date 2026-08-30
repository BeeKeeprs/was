package kr.co.webee.application.gate.dto.request;

import java.time.LocalDateTime;

public record GateTelemetryRequest(
        Double temperature,
        Double humidity,
        LocalDateTime timestamp
) {
}
