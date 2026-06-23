package kr.co.webee.application.hive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.hive.type.GateActionType;

import java.time.LocalTime;

@Schema(description = "개폐기 동작 수정 요청")
public record HiveGateActionUpdateRequest(
        @NotBlank
        @Schema(description = "제목", example = "새벽 환기")
        String title,

        @NotNull
        @Schema(description = "동작 유형", example = "OPEN_ONLY")
        GateActionType actionType,

        @NotNull
        @Schema(description = "동작 시각", example = "09:00:00")
        LocalTime actionTime,

        @Schema(description = "반복 활성화 여부", example = "false")
        boolean repeatEnabled
) {
}
