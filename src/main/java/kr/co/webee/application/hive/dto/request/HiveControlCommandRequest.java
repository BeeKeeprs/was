package kr.co.webee.application.hive.dto.request;

import kr.co.webee.domain.hive.type.ControlMode;
import kr.co.webee.domain.hive.type.ControlType;
import lombok.Builder;

@Builder
public record HiveControlCommandRequest(
        String commandId,
        ControlType type,
        ControlMode mode,
        boolean enabled,
        String responseTopic
) {
    public static HiveControlCommandRequest of(String commandId, ControlType type, ControlMode mode, boolean enabled, String macAddress) {
        return HiveControlCommandRequest.builder()
                .commandId(commandId)
                .type(type)
                .mode(mode)
                .enabled(enabled)
                .responseTopic("hive/%s/control/response".formatted(macAddress))
                .build();
    }
}