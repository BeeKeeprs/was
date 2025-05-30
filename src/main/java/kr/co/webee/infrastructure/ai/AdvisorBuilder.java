package kr.co.webee.infrastructure.ai;

import kr.co.webee.application.ai.RagSearchOptions;
import lombok.ToString;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ToString
public class AdvisorBuilder {

    public static Builder builder(VectorStore vectorStore,
                                  PromptTemplateRegistry promptRegistry,
                                  SimpleLoggerAdvisor loggerAdvisor,
                                  MessageChatMemoryAdvisor memoryAdvisor) {
        return new Builder(vectorStore, promptRegistry, loggerAdvisor, memoryAdvisor);
    }

    @ToString
    public static final class Builder {
        private final List<Advisor> advisors = new ArrayList<>();

        private final VectorStore vectorStore;
        private final PromptTemplateRegistry promptRegistry;
        private final SimpleLoggerAdvisor loggerAdvisor;
        private final MessageChatMemoryAdvisor memoryAdvisor;

        private String conversationId;
        private String input;
        private String promptKey;
        private RagSearchOptions options;

        private boolean hasMemory = false;

        private Builder(VectorStore vectorStore,
                        PromptTemplateRegistry promptRegistry,
                        SimpleLoggerAdvisor loggerAdvisor,
                        MessageChatMemoryAdvisor memoryAdvisor) {
            this.vectorStore = vectorStore;
            this.promptRegistry = promptRegistry;
            this.loggerAdvisor = loggerAdvisor;
            this.memoryAdvisor = memoryAdvisor;
        }

        public Builder input(String input) {
            this.input = input;
            return this;
        }

        public Builder withLogger() {
            advisors.add(loggerAdvisor);
            return this;
        }

        public Builder withMemory(String conversationId) {
            this.hasMemory = true;
            this.conversationId = conversationId;
            advisors.add(memoryAdvisor);
            return this;
        }

        public Builder withRag(String promptKey, RagSearchOptions options) {
            this.promptKey = promptKey;
            this.options = options;
            advisors.add(buildQaAdvisor());
            return this;
        }

        public List<Advisor> build() {
            return List.copyOf(advisors);
        }

        public Optional<String> getConversationId() {
            return hasMemory ? Optional.of(conversationId) : Optional.empty();
        }

        private Advisor buildQaAdvisor() {
            return QuestionAnswerAdvisor.builder(vectorStore)
                    .searchRequest(SearchRequest.builder()
                            .query(input)
                            .topK(options.topK())
                            .similarityThreshold(options.similarityThreshold())
                            .filterExpression(options.filterExpression())
                            .build())
                    .promptTemplate(PromptTemplate.builder()
                            .template(promptRegistry.get(promptKey))
                            .build())
                    .build();
        }
    }
}
