package kr.co.webee.config;

import org.mockito.Mockito;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@TestConfiguration
public class TestAiConfig {

    @Bean
    ChatMemory testChatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    @Bean
    VectorStore testVectorStore() {
        return Mockito.mock(VectorStore.class); // 또는 InMemoryVectorStore 사용 가능
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
        return SimpleLoggerAdvisor.builder().order(Ordered.LOWEST_PRECEDENCE - 1).build();
    }

    @Bean
    ChatModel chatModel() {
        return Mockito.mock(ChatModel.class);
    }
}
