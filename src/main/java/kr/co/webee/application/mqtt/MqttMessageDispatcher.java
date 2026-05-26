package kr.co.webee.application.mqtt;

import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        String payload = (String) message.getPayload();

        dispatch(topic, macAddress, payload);
    }

    private void dispatch(String topic, String macAddress, String payload) {
        MqttMessageHandler handler = handlerMap.get(MqttTopicType.from(topic));

        if (handler == null) {
            // TODO: 예외처리
        }

        handler.handle(payload, macAddress);
    }
}

