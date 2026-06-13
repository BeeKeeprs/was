package kr.co.webee.infrastructure.rabbitmq;

import kr.co.webee.infrastructure.rabbitmq.properties.RabbitMqProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RabbitMessageProducer {
    private final RabbitMqProperties rabbitMqProperties;
    private final RabbitTemplate rabbitTemplate;

    public void send(Object message) {
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getExchangeName(),
                rabbitMqProperties.getRoutingKey(),
                message
        );
    }
}
