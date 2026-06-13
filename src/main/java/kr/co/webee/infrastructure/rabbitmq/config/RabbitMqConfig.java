package kr.co.webee.infrastructure.rabbitmq.config;

import kr.co.webee.infrastructure.rabbitmq.properties.RabbitMqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RabbitMqConfig {
    private static final String DLQ_SUFFIX = ".dlq";

    private final RabbitMqProperties rabbitMqProperties;

    /* 일반 direct exchange */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(rabbitMqProperties.getExchangeName());
    }

    /* DLX(Dead Letter용 exchange) */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(rabbitMqProperties.getExchangeName()+DLQ_SUFFIX);
    }

    /* 일반 큐(기본 메시지 저장) */
    @Bean
    Queue queue() {
        return QueueBuilder.durable(rabbitMqProperties.getQueueName())
                .withArgument("x-dead-letter-exchange", rabbitMqProperties.getExchangeName() + DLQ_SUFFIX)
                .withArgument("x-dead-letter-routing-key", rabbitMqProperties.getRoutingKey() + DLQ_SUFFIX)
                .build();
    }

    /* DLQ(실패 메시지 저장) */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(rabbitMqProperties.getQueueName() + DLQ_SUFFIX, true);
    }

    /* exchange - queue 바인딩 */
    @Bean
    Binding binding(DirectExchange directExchange, Queue queue) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(rabbitMqProperties.getRoutingKey());
    }

    /* DLX - DLQ 바인딩 */
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(rabbitMqProperties.getRoutingKey() + DLQ_SUFFIX);
    }

    /* 메시지 리스너 컨테이너 */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setConcurrentConsumers(2); // 기본 consumer 스레드 수
        factory.setMaxConcurrentConsumers(4); // max consumer 스레드 수
        factory.setPrefetchCount(5); // consumer 1개당 미리 가져올 메시지 개수
        factory.setAdviceChain(retryInterceptor());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO); // 처리 성공 시 ACK 자동 전송
        return factory;
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3) // 최초 실행 1회 + 재시도 2회
                .backOffOptions(500, 2.0, 3000)
                .recoverer((message, cause) -> {
                    log.error("message processing failed after all retries. Moving to DLQ. cause={}", cause.getMessage());
                    new RejectAndDontRequeueRecoverer().recover(message, cause);
                })
                .build();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}