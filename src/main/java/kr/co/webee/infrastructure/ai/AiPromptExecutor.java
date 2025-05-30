package kr.co.webee.infrastructure.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class AiPromptExecutor {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;
    private final PromptTemplateRegistry promptRegistry;
    private final SimpleLoggerAdvisor loggerAdvisor;
    private final MessageChatMemoryAdvisor memoryAdvisor;

    public String run(String input, Consumer<AdvisorBuilder.Builder> config) {
        var builder = AdvisorBuilder.builder(vectorStore, promptRegistry, loggerAdvisor, memoryAdvisor);
        config.accept(builder);
        return buildRequest(input, builder).call().content();
    }

    public <T> T run(String input, Consumer<AdvisorBuilder.Builder> config, Class<T> clazz) {
        return run(input, config, ParameterizedTypeReference.forType(clazz));
    }

    public <T> T run(String input, Consumer<AdvisorBuilder.Builder> config, ParameterizedTypeReference<T> responseType) {
        var builder = AdvisorBuilder.builder(vectorStore, promptRegistry, loggerAdvisor, memoryAdvisor);
        config.accept(builder);
        return buildRequest(input, builder).call().entity(responseType);
    }

    private ChatClient.ChatClientRequestSpec buildRequest(String input, AdvisorBuilder.Builder builder) {
        return chatClient.prompt()
                .system(s -> s.param("language", "Korean"))
                .advisors(a -> {
                    builder.getConversationId().ifPresent(cid ->
                            a.param(ChatMemory.CONVERSATION_ID, cid));
                    a.advisors(builder.build());
                })
                .user(input);
    }
}
