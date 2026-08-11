package kr.co.webee.application.hive.dto;

import lombok.Builder;

@Builder
public record HivePendingCommand(
        Long userId,
        Long hiveId,
        Double targetTemperature,
        Double targetHumidity
) {
    public static final String REDIS_KEY_PREFIX = "hive:command:";

    public static String redisKey(String commandId) {
        return REDIS_KEY_PREFIX + commandId;
    }

    public static HivePendingCommand of(Long userId, Long hiveId, Double targetTemperature, Double targetHumidity) {
        return HivePendingCommand.builder()
                .userId(userId)
                .hiveId(hiveId)
                .targetTemperature(targetTemperature)
                .targetHumidity(targetHumidity)
                .build();
    }
}
