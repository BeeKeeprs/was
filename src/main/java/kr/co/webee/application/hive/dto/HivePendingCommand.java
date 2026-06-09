package kr.co.webee.application.hive.dto;

import kr.co.webee.domain.hive.type.ControlMode;
import kr.co.webee.domain.hive.type.ControlType;
import lombok.Builder;

@Builder
public record HivePendingCommand(
        Long userId,
        Long hiveId,
        ControlType type,
        ControlMode mode,
        Boolean autoEnabled,
        Boolean manualEnabled,
        Boolean isOn
) {
    public static final String REDIS_KEY_PREFIX = "hive:command:";

    public static String redisKey(String commandId) {
        return REDIS_KEY_PREFIX + commandId;
    }

    public static HivePendingCommand createAutoControlCommand(Long userId, Long hiveId, ControlType type, boolean autoEnabled) {
        return HivePendingCommand.builder()
                .userId(userId)
                .hiveId(hiveId)
                .type(type)
                .mode(ControlMode.AUTO)
                .autoEnabled(autoEnabled)
                .build();
    }

    public static HivePendingCommand createManualControlCommand(Long userId, Long hiveId, ControlType type, boolean manualEnabled, Boolean isOn) {
        return HivePendingCommand.builder()
                .userId(userId)
                .hiveId(hiveId)
                .type(type)
                .mode(ControlMode.MANUAL)
                .manualEnabled(manualEnabled)
                .isOn(isOn)
                .build();
    }
}
