package kr.co.webee.application.gate.handler;

import kr.co.webee.application.gate.dto.request.GateTunnelAlertRequest;
import kr.co.webee.application.gate.service.GateTunnelAlertService;
import kr.co.webee.application.mqtt.MqttMessageHandler;
import kr.co.webee.application.mqtt.MqttTopicType;
import kr.co.webee.common.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GateTunnelAlertHandler implements MqttMessageHandler {
    private final GateTunnelAlertService gateTunnelAlertService;
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.GATE_TUNNEL_ALERT;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        GateTunnelAlertRequest request = jsonConverter.convert(payload, GateTunnelAlertRequest.class);
        gateTunnelAlertService.processAlert(request, macAddress);
    }
}
