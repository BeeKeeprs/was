package kr.co.webee.application.gate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.gate.entity.GateTimeCard;
import kr.co.webee.domain.gate.type.GateTimeActionType;
import lombok.Builder;

@Builder
@Schema(description = "개폐기 시간 제어 카드 응답")
public record GateTimeCardResponse(
        @Schema(description = "카드 ID", example = "1")
        Long id,

        @Schema(description = "동작 유형", example = "OPEN_AT")
        GateTimeActionType actionType,

        @Schema(description = "시작 시각 (0~24)", example = "9")
        Integer startHour,

        @Schema(description = "종료 시각 (0~24, WINDOW 전용)", example = "15")
        Integer endHour,

        @Schema(description = "반복 활성화 여부", example = "false")
        boolean repeatEnabled,

        @Schema(description = "메모", example = "남쪽 과수원 아침 개방")
        String memo
) {
    public static GateTimeCardResponse from(GateTimeCard card) {
        return GateTimeCardResponse.builder()
                .id(card.getId())
                .actionType(card.getActionType())
                .startHour(card.getStartHour())
                .endHour(card.getEndHour())
                .repeatEnabled(card.isRepeatEnabled())
                .memo(card.getMemo())
                .build();
    }
}
