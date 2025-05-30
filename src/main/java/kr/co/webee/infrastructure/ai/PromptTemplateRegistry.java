package kr.co.webee.infrastructure.ai;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PromptTemplateRegistry {

    private Map<String, String> promptMap;
    private final PromptProperties promptProperties;

    @PostConstruct
    public void init() {
        this.promptMap = promptProperties.prompts().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        e -> read(e.getValue())
                ));

        log.info("Prompt templates loaded: {}", promptMap.keySet());
    }

    private String read(String location) {
        Resource resource = new DefaultResourceLoader().getResource(location);
        try (InputStream is = resource.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Prompt load failed: " + location, e);
        }
    }

    public String get(String key) {
        if (!promptMap.containsKey(key)) {
            throw new IllegalArgumentException("Prompt not found: " + key);
        }
        return promptMap.get(key);
    }
}
