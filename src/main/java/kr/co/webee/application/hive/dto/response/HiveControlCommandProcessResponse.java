package kr.co.webee.application.hive.dto.response;

import kr.co.webee.domain.hive.type.ControlType;
import lombok.Builder;

@Builder
public record HiveControlCommandProcessResponse(
        String commandId,
        Long hiveId,
        ControlType type,
        boolean success,
        Boolean autoEnabled,
        Boolean manualEnabled,
        Boolean isOn,
        String message
) {
    public static HiveControlCommandProcessResponse autoSuccess(String commandId, Long hiveId, ControlType type, boolean autoEnabled) {
        return HiveControlCommandProcessResponse.builder()
                .commandId(commandId)
                .hiveId(hiveId)
                .type(type)
                .success(true)
                .autoEnabled(autoEnabled)
                .build();
    }

    public static HiveControlCommandProcessResponse manualSuccess(String commandId, Long hiveId, ControlType type, boolean manualEnabled, Boolean isOn) {
        return HiveControlCommandProcessResponse.builder()
                .commandId(commandId)
                .hiveId(hiveId)
                .type(type)
                .success(true)
                .manualEnabled(manualEnabled)
                .isOn(isOn)
                .build();
    }

    public static HiveControlCommandProcessResponse failure(String commandId, Long hiveId, ControlType type, String message) {
        return HiveControlCommandProcessResponse.builder()
                .commandId(commandId)
                .hiveId(hiveId)
                .type(type)
                .success(false)
                .message(message)
                .build();
    }
}
