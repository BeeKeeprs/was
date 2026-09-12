package kr.co.webee.application.gate.handler;

import kr.co.webee.application.gate.dto.GateCommandResultMessage;
import kr.co.webee.application.gate.service.GateCommandService;
import kr.co.webee.application.mqtt.MqttMessageHandler;
import kr.co.webee.application.mqtt.MqttTopicType;
import kr.co.webee.common.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GateCommandResultHandler implements MqttMessageHandler {

    private final GateCommandService gateCommandService;
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.GATE_COMMAND_RESULT;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        GateCommandResultMessage result = jsonConverter.convert(payload, GateCommandResultMessage.class);
        gateCommandService.handleResult(result);
    }
}
