package kr.co.webee.infrastructure.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "app.ai")
public record PromptProperties(
        Map<String, String> prompts
) {
}
