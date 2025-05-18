package kr.co.webee.infrastructure.config.ai;

import kr.co.webee.application.ai.RagSearchOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssistantAiExecutor {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final MessageChatMemoryAdvisor memoryAdvisor;
    private final SimpleLoggerAdvisor loggerAdvisor;

    private final AiPromptHelper aiPromptHelper;

    public List<String> getDocuments(String query, RagSearchOptions options) {
        SearchRequest searchRequest = aiPromptHelper.toSearchRequest(query, options);

        List<Document> docs = vectorStore.similaritySearch(searchRequest);
        if (docs == null || docs.isEmpty()) return List.of();
        return docs.stream().map(Document::getText).toList();
    }

    public String generateSimple(String userInput, String conversationId) {
       return aiPromptHelper.buildConversationalPrompt(conversationId, userInput, List.of(memoryAdvisor,loggerAdvisor))
               .call().content();
    }

    public String generateWithRag(String userInput, String conversationId, String prompt, RagSearchOptions options) {
        return aiPromptHelper.buildConversationalPrompt(conversationId, userInput, List.of(
                loggerAdvisor,
                memoryAdvisor,
                aiPromptHelper.buildQaAdvisor(userInput, options, prompt)
                )).call().content();
    }
}
