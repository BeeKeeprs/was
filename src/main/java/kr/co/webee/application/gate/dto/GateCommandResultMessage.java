package kr.co.webee.application.gate.dto;

public record GateCommandResultMessage(
        String commandId,
        String gateId,
        String status,   // "OK" | "ERROR"
        String detail
) {
}
