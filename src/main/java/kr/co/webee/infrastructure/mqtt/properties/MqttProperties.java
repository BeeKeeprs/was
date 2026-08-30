package kr.co.webee.infrastructure.mqtt.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    private String brokerUrl;
    private String inboundClientId;
    private String outboundClientId;
    private List<String> topicFilters;
    private String username;
    private String password;
}
