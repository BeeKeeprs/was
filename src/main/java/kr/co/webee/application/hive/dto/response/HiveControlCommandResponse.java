package kr.co.webee.application.hive.dto.response;

public record HiveControlCommandResponse(
        String commandId,
        boolean success,
        String message
) {
}
