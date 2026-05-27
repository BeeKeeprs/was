package kr.co.webee.application.hive.handler;

import kr.co.webee.application.hive.dto.request.HiveBeeCountRequest;
import kr.co.webee.application.hive.service.HiveBeeCountService;
import kr.co.webee.application.mqtt.MqttMessageHandler;
import kr.co.webee.application.mqtt.MqttTopicType;
import kr.co.webee.common.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HiveBeeCountHandler implements MqttMessageHandler {
    private final HiveBeeCountService hiveBeeCountService;
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.BEE_COUNT;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        HiveBeeCountRequest request = jsonConverter.convert(payload, HiveBeeCountRequest.class);

        hiveBeeCountService.recordBeeCount(request, macAddress);
    }
}
