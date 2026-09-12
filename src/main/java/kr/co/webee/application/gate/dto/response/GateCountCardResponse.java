package kr.co.webee.application.gate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.gate.entity.GateCountCard;
import lombok.Builder;

@Builder
@Schema(description = "개폐기 마릿수 제어 카드 응답")
public record GateCountCardResponse(
        @Schema(description = "카드 ID", example = "1")
        Long id,

        @Schema(description = "요일 비트마스크 (bit 0=일, 1=월, 2=화, 3=수, 4=목, 5=금, 6=토)", example = "62")
        int repeatDays,

        @Schema(description = "마릿수 구간 최솟값", example = "3")
        int minCount,

        @Schema(description = "마릿수 구간 최댓값", example = "12")
        int maxCount,

        @Schema(description = "시작 시각 (0~24, null이면 하루 종일)", example = "0")
        Integer startHour,

        @Schema(description = "종료 시각 (0~24, null이면 하루 종일)", example = "4")
        Integer endHour,

        @Schema(description = "minCount~maxCount 구간 입구 열림 여부", example = "true")
        boolean withinEntranceOpen,

        @Schema(description = "minCount~maxCount 구간 출구 열림 여부", example = "true")
        boolean withinExitOpen,

        @Schema(description = "maxCount 이상 구간 입구 열림 여부", example = "true")
        boolean aboveEntranceOpen,

        @Schema(description = "maxCount 이상 구간 출구 열림 여부", example = "false")
        boolean aboveExitOpen,

        @Schema(description = "메모", example = "남쪽 과수원")
        String memo
) {
    public static GateCountCardResponse from(GateCountCard card) {
        return GateCountCardResponse.builder()
                .id(card.getId())
                .repeatDays(card.getRepeatDays())
                .minCount(card.getMinCount())
                .maxCount(card.getMaxCount())
                .startHour(card.getStartHour())
                .endHour(card.getEndHour())
                .withinEntranceOpen(card.isWithinEntranceOpen())
                .withinExitOpen(card.isWithinExitOpen())
                .aboveEntranceOpen(card.isAboveEntranceOpen())
                .aboveExitOpen(card.isAboveExitOpen())
                .memo(card.getMemo())
                .build();
    }
}
