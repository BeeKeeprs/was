package kr.co.webee.config;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {
	
    private static final String REDIS_DOCKER_IMAGE = "redis:7.2.0-alpine";
    public static final String ECLIPSE_MOSQUITTO_DOCKER_IMAGE = "eclipse-mosquitto:2.0";
    private static final int REDIS_PORT = 6379;
    private static final int MQTT_PORT = 1883;

    @Bean
    @ServiceConnection
    MySQLContainer<?> mySQLContainer() {
        return new MySQLContainer<>(
                DockerImageName.parse("mysql:8.0.34").asCompatibleSubstituteFor("mysql"));
    }
    
    @Container
    @ServiceConnection
    static RedisContainer redisContainer;

    @Container
    static GenericContainer<?> mosquittoContainer;

    static {
        redisContainer = new RedisContainer(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                .withExposedPorts(REDIS_PORT)
                .withReuse(true)
                .waitingFor(Wait.forListeningPort());

        redisContainer.start();

        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redisContainer.getMappedPort(REDIS_PORT)));
        System.setProperty("spring.data.redis.password", ""); // no password on test
    }

    static{
        mosquittoContainer = new GenericContainer<>(DockerImageName.parse(ECLIPSE_MOSQUITTO_DOCKER_IMAGE))
                .withExposedPorts(MQTT_PORT)
                .withReuse(true)
                .waitingFor(Wait.forListeningPort())
                .withCommand("mosquitto", "-c", "/mosquitto/config/mosquitto.conf");

        mosquittoContainer.start();

        System.setProperty("mqtt.broker-url",
                "tcp://" + mosquittoContainer.getHost() + ":" + mosquittoContainer.getMappedPort(MQTT_PORT));
    }
}
