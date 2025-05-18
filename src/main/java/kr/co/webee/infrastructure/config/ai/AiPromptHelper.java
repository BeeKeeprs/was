package kr.co.webee.infrastructure.config.ai;

import kr.co.webee.application.ai.RagSearchOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AiPromptHelper {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ChatClient.ChatClientRequestSpec buildConversationalPrompt(String conversationId, String query, List<Advisor> advisors) {
        return chatClient.prompt()
                .system(s -> s.param("language", "Korean").param("character", "챗봇"))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId).advisors(advisors))
                .user(query);
    }

    public ChatClient.ChatClientRequestSpec buildStatelessPrompt(String query, List<Advisor> advisors) {
        return chatClient.prompt()
                .system(system -> system.param("language", "Korean"))
                .advisors(advisor -> advisor.advisors(advisors))
                .user(query);
    }

    public QuestionAnswerAdvisor buildQaAdvisor(String query, RagSearchOptions options, String prompt) {
        return QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(toSearchRequest(query, options))
                .promptTemplate(PromptTemplate.builder()
                        .template(prompt)
                        .build())
                .build();
    }

    public SearchRequest toSearchRequest(String query, RagSearchOptions options) {
        return SearchRequest.builder()
                .query(query)
                .topK(Optional.ofNullable(options.topK()).orElse(10))
                .similarityThreshold(Optional.ofNullable(options.similarityThreshold()).orElse(0.75))
                .filterExpression(options.filterExpression())
                .build();
    }
}
