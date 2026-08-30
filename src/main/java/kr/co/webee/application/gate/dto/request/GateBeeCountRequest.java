package kr.co.webee.application.gate.dto.request;

import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.entity.GateBeeCount;

import java.time.LocalDateTime;

public record GateBeeCountRequest(
        Integer entranceIn,
        Integer entranceOut,
        Integer exitIn,
        Integer exitOut,
        LocalDateTime timestamp
) {
    public GateBeeCount toEntity(Gate gate) {
        return GateBeeCount.builder()
                .entranceIn(entranceIn)
                .entranceOut(entranceOut)
                .exitIn(exitIn)
                .exitOut(exitOut)
                .recordedAt(timestamp)
                .gate(gate)
                .build();
    }
}
