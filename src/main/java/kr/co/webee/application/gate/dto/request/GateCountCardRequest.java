package kr.co.webee.application.gate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.entity.GateCountCard;

@Schema(description = "개폐기 마릿수 제어 카드 요청")
public record GateCountCardRequest(
        @Schema(description = "요일 비트마스크 (bit 0=일, 1=월, 2=화, 3=수, 4=목, 5=금, 6=토)", example = "62")
        int repeatDays,

        @Schema(description = "마릿수 구간 최솟값", example = "3")
        @Min(0)
        int minCount,

        @Schema(description = "마릿수 구간 최댓값", example = "12")
        @Min(1)
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
    public GateCountCard toEntity(Gate gate) {
        return GateCountCard.builder()
                .gate(gate)
                .repeatDays(repeatDays)
                .minCount(minCount)
                .maxCount(maxCount)
                .startHour(startHour)
                .endHour(endHour)
                .withinEntranceOpen(withinEntranceOpen)
                .withinExitOpen(withinExitOpen)
                .aboveEntranceOpen(aboveEntranceOpen)
                .aboveExitOpen(aboveExitOpen)
                .memo(memo)
                .build();
    }
}
