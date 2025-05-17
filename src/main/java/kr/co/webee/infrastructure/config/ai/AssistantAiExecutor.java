package kr.co.webee.infrastructure.config.ai;

import kr.co.webee.application.ai.RagSearchOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssistantAiExecutor {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final MessageChatMemoryAdvisor memoryAdvisor;
    private final SimpleLoggerAdvisor loggerAdvisor;

    public List<String> getDocuments(String query, RagSearchOptions options) {
        SearchRequest searchRequest = toSearchRequest(query, options);

        List<Document> docs = vectorStore.similaritySearch(searchRequest);
        if (docs == null || docs.isEmpty()) return List.of();
        return docs.stream().map(Document::getText).toList();
    }

    public String generateSimple(String userInput, String conversationId) {
        return buildPrompt(userInput, conversationId, List.of(loggerAdvisor, memoryAdvisor))
                .call()
                .content();
    }

    public String generateWithRag(String userInput, String conversationId, String prompt, RagSearchOptions options) {
        return buildPrompt(userInput, conversationId, List.of(
                loggerAdvisor,
                memoryAdvisor,
                buildQaAdvisor(userInput, prompt, options)
        )).call().content();
    }

    private Advisor buildQaAdvisor(String userInput, String prompt, RagSearchOptions options) {
        return QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(toSearchRequest(userInput, options))
                .promptTemplate(PromptTemplate.builder()
                        .template(prompt)
                        .variables(Map.of("userInput", userInput))
                        .build())
                .build();
    }

    private SearchRequest toSearchRequest(String query, RagSearchOptions options) {
        return SearchRequest.builder()
                .query(query)
                .topK(Optional.ofNullable(options.topK()).orElse(10))
                .similarityThreshold(Optional.ofNullable(options.similarityThreshold()).orElse(0.75))
                .filterExpression(options.filterExpression())
                .build();
    }

    private ChatClient.ChatClientRequestSpec buildPrompt(String userInput, String conversationId, List<Advisor> advisors) {
        return chatClient.prompt()
                .system(s -> s.param("language", "Korean").param("character", "챗봇"))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId).advisors(advisors))
                .user(userInput);
    }
}
