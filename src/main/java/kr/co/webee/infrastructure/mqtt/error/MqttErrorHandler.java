package kr.co.webee.infrastructure.mqtt.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttErrorHandler {

    @ServiceActivator(inputChannel = "mqttErrorChannel")
    public void handle(ErrorMessage errorMessage) {
        MessagingException exception = (MessagingException) errorMessage.getPayload();

        Message<?> failedMessage = exception.getFailedMessage();

        Throwable cause = exception.getCause();

        log.error(
                "MQTT processing failed. topic={}, payload={}",
                failedMessage.getHeaders().get(MqttHeaders.RECEIVED_TOPIC),
                failedMessage.getPayload(),
                cause
        );
    }
}
