package kr.co.webee.application.gate.handler;

import kr.co.webee.application.gate.dto.request.GateConnectionRequest;
import kr.co.webee.application.gate.service.GateConnectionService;
import kr.co.webee.application.mqtt.MqttMessageHandler;
import kr.co.webee.application.mqtt.MqttTopicType;
import kr.co.webee.common.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GateConnectionHandler implements MqttMessageHandler {

    private final GateConnectionService gateConnectionService;
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.GATE_CONNECTION;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        GateConnectionRequest request = jsonConverter.convert(payload, GateConnectionRequest.class);
        gateConnectionService.recordConnection(request, macAddress);
    }
}
