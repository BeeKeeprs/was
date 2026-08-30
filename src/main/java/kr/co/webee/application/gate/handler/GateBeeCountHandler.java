package kr.co.webee.application.gate.handler;

import kr.co.webee.application.gate.dto.request.GateBeeCountRequest;
import kr.co.webee.application.gate.service.GateBeeCountService;
import kr.co.webee.application.mqtt.MqttMessageHandler;
import kr.co.webee.application.mqtt.MqttTopicType;
import kr.co.webee.common.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GateBeeCountHandler implements MqttMessageHandler {
    private final GateBeeCountService gateBeeCountService;
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.GATE_BEE_COUNT;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        GateBeeCountRequest request = jsonConverter.convert(payload, GateBeeCountRequest.class);
        gateBeeCountService.recordBeeCount(request, macAddress);
    }
}
