package kr.co.webee.application.gate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.gate.entity.GateCommand;
import kr.co.webee.domain.gate.type.GateCommandStatus;

@Schema(description = "개폐기 카드 실행 접수 응답")
public record GateCommandAcceptedResponse(
        @Schema(description = "명령 ID", example = "a1b2c3d4-...")
        String commandId,

        @Schema(description = "개폐기 MAC 주소", example = "AA:BB:CC:DD:EE:FF")
        String gateId,

        @Schema(description = "상태", example = "PENDING")
        GateCommandStatus status
) {
    public static GateCommandAcceptedResponse from(GateCommand command) {
        return new GateCommandAcceptedResponse(
                command.getId(),
                command.getGate().getMacAddress(),
                command.getStatus()
        );
    }
}
