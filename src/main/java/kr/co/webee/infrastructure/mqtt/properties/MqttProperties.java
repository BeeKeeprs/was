package kr.co.webee.infrastructure.mqtt.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    private String brokerUrl;
    private String inboundClientId;
    private String outboundClientId;
    private String topicFilter;
    private String username;
    private String password;
}
