package kr.co.webee.application.gate.dto.request;

import java.time.LocalDateTime;

public record GateTunnelAlertRequest(
        String tunnel,
        int sensor,
        int blockedSeconds,
        boolean servoDeferred,
        LocalDateTime occurredAt
) {
}
