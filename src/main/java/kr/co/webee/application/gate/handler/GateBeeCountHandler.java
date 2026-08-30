package kr.co.webee.application.gate.handler;

import kr.co.webee.application.gate.dto.request.GateBeeCountRequest;
import kr.co.webee.application.mqtt.MqttMessageHandler;
import kr.co.webee.application.mqtt.MqttTopicType;
import kr.co.webee.common.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GateBeeCountHandler implements MqttMessageHandler {
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.GATE_BEE_COUNT;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        GateBeeCountRequest request = jsonConverter.convert(payload, GateBeeCountRequest.class);

        // TODO: 서비스 위밈
    }
}
