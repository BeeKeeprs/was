package kr.co.webee.infrastructure.mqtt.config;

import kr.co.webee.infrastructure.mqtt.MqttMessageSubscriber;
import kr.co.webee.infrastructure.mqtt.properties.MqttProperties;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

@RequiredArgsConstructor
@Configuration
public class MqttBrokerConfig {
    private final MqttProperties properties;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() { // MQTT 클라이언트 관련 설정
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{properties.getBrokerUrl()});
        options.setUserName(properties.getUsername());
        options.setPassword(properties.getPassword().toCharArray());
        options.setAutomaticReconnect(true);

        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageProducer inboundChannel(
            MqttPahoClientFactory mqttClientFactory,
            MessageChannel mqttInputChannel
    ) { // inboundChannel 어댑터
        var adapter = new MqttPahoMessageDrivenChannelAdapter(
                properties.getInboundClientId(),
                mqttClientFactory,
                properties.getTopicFilter()
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel);

        return adapter;
    }

    @Bean
    public MessageChannel mqttInputChannel() { // inboundChannel
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel") // MQTT 구독 핸들러
    public MessageHandler inboundMessageHandler() {
        return new MqttMessageSubscriber();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() { // outboundChannel 어댑터
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(properties.getOutboundClientId(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setAsyncEvents(true);
        messageHandler.setDefaultQos(1);
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() { // outboundChannel
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MyGateway { // 메시지 발행 게이트웨이
        void publish(@Header(MqttHeaders.TOPIC) String topic, String payload);
    }
}
