package kr.co.webee.application.hive.dto.response;

import lombok.Builder;

@Builder
public record HiveControlCommandProcessResponse(
        String commandId,
        Long hiveId,
        boolean success,
        Double targetTemperature,
        Double targetHumidity,
        String message
) {
    public static HiveControlCommandProcessResponse success(String commandId, Long hiveId, Double targetTemperature, Double targetHumidity) {
        return HiveControlCommandProcessResponse.builder()
                .commandId(commandId)
                .hiveId(hiveId)
                .success(true)
                .targetTemperature(targetTemperature)
                .targetHumidity(targetHumidity)
                .build();
    }

    public static HiveControlCommandProcessResponse failure(String commandId, Long hiveId, String message) {
        return HiveControlCommandProcessResponse.builder()
                .commandId(commandId)
                .hiveId(hiveId)
                .success(false)
                .message(message)
                .build();
    }
}
