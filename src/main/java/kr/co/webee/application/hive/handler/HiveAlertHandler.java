package kr.co.webee.application.hive.handler;

import kr.co.webee.application.hive.dto.request.HiveAlertRequest;
import kr.co.webee.application.hive.service.HiveAlertService;
import kr.co.webee.application.mqtt.MqttMessageHandler;
import kr.co.webee.application.mqtt.MqttTopicType;
import kr.co.webee.common.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HiveAlertHandler implements MqttMessageHandler {
    private final HiveAlertService hiveAlertService;
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.ALERT;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        HiveAlertRequest request = jsonConverter.convert(payload, HiveAlertRequest.class);
        hiveAlertService.processAlert(request, macAddress);
    }
}
