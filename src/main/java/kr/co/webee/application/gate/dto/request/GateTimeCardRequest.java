package kr.co.webee.application.gate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.entity.GateTimeCard;
import kr.co.webee.domain.gate.type.GateTimeActionType;

@Schema(description = "개폐기 시간 제어 카드 요청")
public record GateTimeCardRequest(
        @Schema(description = "동작 유형", example = "OPEN_AT")
        @NotNull
        GateTimeActionType actionType,

        @Schema(description = "시작 시각 (0~24, ALTERNATE_24H면 null)", example = "9")
        Integer startHour,

        @Schema(description = "종료 시각 (0~24, WINDOW 전용)", example = "15")
        Integer endHour,

        @Schema(description = "반복 활성화 여부", example = "false")
        boolean repeatEnabled,

        @Schema(description = "메모", example = "남쪽 과수원 아침 개방")
        String memo
) {
    public GateTimeCard toEntity(Gate gate) {
        return GateTimeCard.builder()
                .gate(gate)
                .actionType(actionType)
                .startHour(startHour)
                .endHour(endHour)
                .repeatEnabled(repeatEnabled)
                .memo(memo)
                .build();
    }
}
