package kr.co.webee.infrastructure.config.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore.MetadataField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import redis.clients.jedis.JedisPooled;

import java.net.URI;

@Configuration
public class AiConfig {

    @Value("${app.ai.max-messages}")
    private int maxMessageSize;

    @Bean
    ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(maxMessageSize)
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    @Bean
    VectorStore vectorStore(
            EmbeddingModel embeddingModel,
            @Value("${spring.ai.vector-store.redis.uri}") String redisUri,
            @Value("${spring.ai.vector-store.redis.index}") String index,
            @Value("${spring.ai.vector-store.redis.prefix}") String prefix,
            @Value("${spring.ai.vector-store.redis.initialize-schema}") boolean initializeSchema) {

        return RedisVectorStore.builder(new JedisPooled(URI.create(redisUri)), embeddingModel)
                .indexName(index)
                .prefix(prefix)
                .metadataFields(
                        MetadataField.tag("category"),
                        MetadataField.tag("type"),
                        MetadataField.tag("created_by"),
                        MetadataField.tag("origin"),
                        MetadataField.numeric("confidence"),
                        MetadataField.text("created_at")
                )
                .initializeSchema(initializeSchema)
                .build();
    }

    @Bean
    MessageChatMemoryAdvisor memoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }

    @Bean
    ChatClient baseChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    SimpleLoggerAdvisor loggerAdvisor() {
        return SimpleLoggerAdvisor.builder()
                .order(Ordered.LOWEST_PRECEDENCE - 1)
                .build();
    }
}
