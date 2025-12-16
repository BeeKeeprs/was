package kr.co.webee.annotation;

import kr.co.webee.config.AwsS3TestConfig;
import kr.co.webee.config.TestAiConfig;
import kr.co.webee.config.TestcontainersConfig;
import org.springframework.ai.model.openai.autoconfigure.OpenAiAudioSpeechAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@ImportAutoConfiguration(exclude = {
        OpenAiAudioSpeechAutoConfiguration.class,
})
@Import({TestcontainersConfig.class, AwsS3TestConfig.class, TestAiConfig.class})
public @interface IntegrationTest {
}
