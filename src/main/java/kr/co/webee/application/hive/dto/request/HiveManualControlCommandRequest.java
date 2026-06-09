package kr.co.webee.application.hive.dto.request;

import kr.co.webee.domain.hive.type.ControlMode;
import kr.co.webee.domain.hive.type.ControlType;
import lombok.Builder;

@Builder
public record HiveManualControlCommandRequest(
        String commandId,
        ControlType type,
        ControlMode mode,
        boolean enabled,
        Boolean isOn,
        String responseTopic
) {
    public static HiveManualControlCommandRequest of(String commandId, ControlType type, boolean enabled, Boolean isOn, String macAddress) {
        return HiveManualControlCommandRequest.builder()
                .commandId(commandId)
                .type(type)
                .mode(ControlMode.MANUAL)
                .enabled(enabled)
                .isOn(isOn)
                .responseTopic("hive/%s/control/response".formatted(macAddress))
                .build();
    }
}
