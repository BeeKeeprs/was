package kr.co.webee.application.gate.dto.response;

import kr.co.webee.domain.gate.entity.Gate;

import java.time.LocalDateTime;

public record GateConnectionResponse(
        boolean isConnected,
        LocalDateTime lastConnectedAt
) {
    public static GateConnectionResponse from(Gate gate) {
        return new GateConnectionResponse(gate.isConnected(), gate.getLastConnectedAt());
    }
}
