package kr.co.webee.config;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {
	
    private static final String REDIS_DOCKER_IMAGE = "redis:7.2.0-alpine";
    private static final int REDIS_PORT = 6379;
	
    @Bean
    @ServiceConnection
    MySQLContainer<?> mySQLContainer() {
        return new MySQLContainer<>(
                DockerImageName.parse("mysql:8.0.34").asCompatibleSubstituteFor("mysql"));
    }
    
    @Container
    @ServiceConnection
    static RedisContainer redisContainer;

    static {
        redisContainer = new RedisContainer(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                .withExposedPorts(REDIS_PORT).withReuse(true).waitingFor(Wait.forListeningPort());

        redisContainer.start();

        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redisContainer.getMappedPort(REDIS_PORT)));
        System.setProperty("spring.data.redis.password", ""); // no password on test
    }
}
