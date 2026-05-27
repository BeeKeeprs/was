package kr.co.webee.application.hive.handler;

import kr.co.webee.application.hive.dto.request.HiveConnectionRequest;
import kr.co.webee.application.hive.service.HiveConnectionService;
import kr.co.webee.application.mqtt.MqttMessageHandler;
import kr.co.webee.application.mqtt.MqttTopicType;
import kr.co.webee.common.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HiveConnectionHandler implements MqttMessageHandler {
    private final HiveConnectionService hiveConnectionService;
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.CONNECTION;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        HiveConnectionRequest request = jsonConverter.convert(payload, HiveConnectionRequest.class);

        hiveConnectionService.recordConnection(request, macAddress);
    }
}
