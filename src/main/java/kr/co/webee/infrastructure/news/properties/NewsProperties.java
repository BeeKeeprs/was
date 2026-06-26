package kr.co.webee.infrastructure.news.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "news")
@Component
public class NewsProperties {
    private String clientId;
    private String clientSecret;
    private String apiUrl;
}
