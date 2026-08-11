package kr.co.webee.application.hive.dto.request;

import lombok.Builder;

@Builder
public record HiveManualControlCommandRequest(
        String commandId,
        Double targetTemperature,
        Double targetHumidity,
        String responseTopic
) {
    public static HiveManualControlCommandRequest of(String commandId, Double targetTemperature, Double targetHumidity, String macAddress) {
        return HiveManualControlCommandRequest.builder()
                .commandId(commandId)
                .targetTemperature(targetTemperature)
                .targetHumidity(targetHumidity)
                .responseTopic("hive/%s/control/response".formatted(macAddress))
                .build();
    }
}
