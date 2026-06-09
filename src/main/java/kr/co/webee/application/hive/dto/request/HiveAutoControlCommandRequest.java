package kr.co.webee.application.hive.dto.request;

import kr.co.webee.domain.hive.type.ControlMode;
import kr.co.webee.domain.hive.type.ControlType;
import lombok.Builder;

@Builder
public record HiveAutoControlCommandRequest(
        String commandId,
        ControlType type,
        ControlMode mode,
        boolean enabled,
        String responseTopic
) {
    public static HiveAutoControlCommandRequest of(String commandId, ControlType type, boolean enabled, String macAddress) {
        return HiveAutoControlCommandRequest.builder()
                .commandId(commandId)
                .type(type)
                .mode(ControlMode.AUTO)
                .enabled(enabled)
                .responseTopic("hive/%s/control/response".formatted(macAddress))
                .build();
    }
}
