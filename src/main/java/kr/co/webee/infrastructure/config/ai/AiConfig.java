package kr.co.webee.infrastructure.config.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore.MetadataField;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPooled;

import java.net.URI;

@Configuration
public class AiConfig {

    @Value("${app.ai.system-prompt}")
    private String systemPrompt;

    @Value("${app.ai.rag-prompt}")
    private String ragPrompt;

    @Value("${app.ai.max-messages}")
    private int maxMessageSize;

    @Value("${app.ai.vector-store.topK}")
    private int topK;

    @Value("${app.ai.vector-store.similarity-threshold}")
    private double similarityThreshold;

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
    @Qualifier("vanillaChatClientWithMemory")
    public ChatClient vanillaChatClientWithMemory(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @Bean
    @Qualifier("ragChatDefaultClient")
    ChatClient ragChatDefaultClient(ChatClient.Builder builder, VectorStore vectorStore, ChatMemory chatMemory) {
        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(similarityThreshold)
                .topK(topK)
                .build();

        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .queryAugmenter(ContextualQueryAugmenter.builder().build())
                .build();

        return builder
                .defaultAdvisors(
                        advisor,
                        MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    @Qualifier("ragChatCustomClient")
    ChatClient ragChatCustomClient(ChatClient.Builder builder, ChatMemory chatMemory, VectorStore store) {
        SearchRequest request = SearchRequest.builder()
                .topK(topK)
                .similarityThreshold(similarityThreshold)
                .build();

        PromptTemplate template = PromptTemplate.builder()
                .template(ragPrompt)
                .build();

        QuestionAnswerAdvisor qaAdvisor = QuestionAnswerAdvisor.builder(store)
                .searchRequest(request)
                .promptTemplate(template)
                .build();

        return builder
                .defaultSystem(systemPrompt)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1),
                        qaAdvisor
                )
                .build();
    }
}
