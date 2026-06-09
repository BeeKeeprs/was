package kr.co.webee.presentation.hive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.type.ControlType;

@Schema(description = "벌통 센서 자동제어 설정 request")
public record HiveAutoControlRequest(
        @Schema(description = "제어 센서 타입", example = "TEMPERATURE")
        @NotNull
        ControlType type,

        @Schema(description = "자동제어 활성화 여부", example = "true")
        @NotNull
        Boolean enabled
) {
}
