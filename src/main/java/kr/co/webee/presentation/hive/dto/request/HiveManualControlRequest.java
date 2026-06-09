package kr.co.webee.presentation.hive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.hive.type.ControlType;

@Schema(description = "벌통 센서 수동제어 요청 request")
public record HiveManualControlRequest(
        @Schema(description = "제어 센서 타입", example = "FAN")
        @NotNull
        ControlType type,

        @Schema(description = "수동제어 활성화 여부", example = "true")
        @NotNull
        Boolean enabled,

        @Schema(description = "센서 ON/OFF (enabled=true일 때만 유효)", example = "true", nullable = true)
        @NotNull
        Boolean isOn
) {
}
