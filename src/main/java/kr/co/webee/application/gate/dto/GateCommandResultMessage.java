package kr.co.webee.application.gate.dto;

public record GateCommandResultMessage(
        String commandId,
        String gateId,
        String status,              // "OK" | "ERROR"
        String detail,
        String executionCommandId,  // 상태가 바뀐 실행 commandId (nullable)
        String executionStatus      // "ACTIVE" | "CANCELLED" (nullable)
) {
}
