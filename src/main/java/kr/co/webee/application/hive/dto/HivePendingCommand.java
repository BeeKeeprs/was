package kr.co.webee.application.hive.dto;

import kr.co.webee.domain.hive.type.ControlType;
import lombok.Builder;

@Builder
public record HivePendingCommand(
        Long userId,
        Long hiveId,
        ControlType type,
        boolean autoEnabled
) {
    public static final String REDIS_KEY_PREFIX = "hive:command:";

    public static String redisKey(String commandId) {
        return REDIS_KEY_PREFIX + commandId;
    }

    public static HivePendingCommand of(Long userId, Long hiveId, ControlType type, boolean autoEnabled) {
        return HivePendingCommand.builder()
                .userId(userId)
                .hiveId(hiveId)
                .type(type)
                .autoEnabled(autoEnabled)
                .build();
    }
}
