package kr.co.webee.application.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MqttMessageDispatcher implements MessageHandler {
    private final Map<MqttTopicType, MqttMessageHandler> handlerMap;

    public MqttMessageDispatcher(List<MqttMessageHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(MqttMessageHandler::getTopicType, handler -> handler));
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String macAddress = topic.split("/")[1];
        Object payload = message.getPayload();

        dispatch(topic, macAddress, payload);
    }

    private void dispatch(String topic, String macAddress, Object payload) {
        MqttMessageHandler handler = handlerMap.get(MqttTopicType.from(topic));

        if (handler == null) {
            throw new IllegalStateException("topic: %s에 대한 handler가 존재하지 않습니다.".formatted(topic));
        }

        handler.handle(payload, macAddress);
    }
}
