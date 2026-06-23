package kr.co.webee.application.hive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.entity.HiveGateAction;
import kr.co.webee.domain.hive.type.GateActionType;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@Schema(description = "개폐기 동작 단건 조회 응답")
public record HiveGateActionDetailResponse(
        @Schema(description = "개폐기 동작 ID", example = "1")
        Long id,

        @Schema(description = "제목", example = "새벽 환기")
        String title,

        @Schema(description = "동작 유형", example = "OPEN_ONLY")
        GateActionType actionType,

        @Schema(description = "동작 시각", example = "09:00:00")
        LocalTime actionTime,

        @Schema(description = "반복 활성화 여부", example = "false")
        boolean repeatEnabled
) {
    public static HiveGateActionDetailResponse from(HiveGateAction action) {
        return HiveGateActionDetailResponse.builder()
                .id(action.getId())
                .title(action.getTitle())
                .actionType(action.getActionType())
                .actionTime(action.getActionTime())
                .repeatEnabled(action.isRepeatEnabled())
                .build();
    }
}
