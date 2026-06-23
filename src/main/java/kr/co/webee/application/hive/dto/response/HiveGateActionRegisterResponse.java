package kr.co.webee.application.hive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.entity.HiveGateAction;

@Schema(description = "개폐기 동작 등록 response")
public record HiveGateActionRegisterResponse(
        @Schema(description = "개폐기 동작 ID", example = "1")
        Long id
) {
    public static HiveGateActionRegisterResponse of(HiveGateAction hiveGateAction) {
        return new HiveGateActionRegisterResponse(hiveGateAction.getId());
    }
}
