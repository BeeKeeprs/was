package kr.co.webee.application.hive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveGateAction;
import kr.co.webee.domain.hive.type.GateActionType;

import java.time.LocalTime;

@Schema(description = "개폐기 동작 등록 request")
public record HiveGateActionRegisterRequest(
        @Schema(description = "개폐기 동작 제목", example = "새벽 환기")
        @NotBlank
        String title,

        @Schema(description = "개폐기 동작 유형", example = "OPEN_ONLY")
        @NotNull
        GateActionType actionType,

        @Schema(description = "동작 시각", example = "09:00:00")
        @NotNull
        LocalTime actionTime,

        @Schema(description = "반복 활성화 여부", example = "false")
        boolean repeatEnabled
) {
    public HiveGateAction toEntity(Hive hive) {
        return HiveGateAction.builder()
                .hive(hive)
                .title(title)
                .actionType(actionType)
                .actionTime(actionTime)
                .repeatEnabled(repeatEnabled)
                .build();
    }
}
