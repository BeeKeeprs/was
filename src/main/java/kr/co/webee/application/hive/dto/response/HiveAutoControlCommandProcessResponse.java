package kr.co.webee.application.hive.dto.response;

import kr.co.webee.domain.hive.type.ControlType;
import lombok.Builder;

@Builder
public record HiveAutoControlCommandProcessResponse(
        String commandId,
        Long hiveId,
        ControlType type,
        boolean success,
        Boolean autoEnabled,
        String message
) {
    public static HiveAutoControlCommandProcessResponse success(String commandId, Long hiveId, ControlType type, boolean autoEnabled) {
        return HiveAutoControlCommandProcessResponse.builder()
                .commandId(commandId)
                .hiveId(hiveId)
                .type(type)
                .success(true)
                .autoEnabled(autoEnabled)
                .build();
    }

    public static HiveAutoControlCommandProcessResponse failure(String commandId, Long hiveId, ControlType type, String message) {
        return HiveAutoControlCommandProcessResponse.builder()
                .commandId(commandId)
                .hiveId(hiveId)
                .type(type)
                .success(false)
                .message(message)
                .build();
    }
}
