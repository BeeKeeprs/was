package kr.co.webee.application.gate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.gate.entity.GateCommand;
import kr.co.webee.domain.gate.type.GateCommandStatus;

@Schema(description = "개폐기 명령 상태 조회 응답")
public record GateCommandStatusResponse(
        @Schema(description = "명령 ID")
        String commandId,

        @Schema(description = "상태", example = "SUCCESS")
        GateCommandStatus status,

        @Schema(description = "실패 사유 (FAILED일 때만)", example = "null")
        String detail
) {
    public static GateCommandStatusResponse from(GateCommand command) {
        return new GateCommandStatusResponse(
                command.getId(),
                command.getStatus(),
                command.getDetail()
        );
    }
}
