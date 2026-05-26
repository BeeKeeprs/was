package kr.co.webee.application.mqtt;

import kr.co.webee.application.hive.dto.HiveTelemetryRequest;
import kr.co.webee.application.hive.service.HiveTelemetryService;
import kr.co.webee.common.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TelemetryHandler implements MqttMessageHandler{
    private final HiveTelemetryService hiveTelemetryService;
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.TELEMETRY;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        HiveTelemetryRequest request = jsonConverter.convert(payload, HiveTelemetryRequest.class);

        hiveTelemetryService.recordTelemetry(request, macAddress);
    }
}
