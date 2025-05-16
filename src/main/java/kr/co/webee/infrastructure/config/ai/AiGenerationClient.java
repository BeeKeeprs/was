package kr.co.webee.infrastructure.config.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiGenerationClient {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final MessageChatMemoryAdvisor memoryAdvisor;
    private final SimpleLoggerAdvisor loggerAdvisor;

    @Value("${app.ai.rag-prompt}")
    private String ragPrompt;

    @Value("${app.ai.vector-store.topK}")
    private int topK;

    @Value("${app.ai.vector-store.similarity-threshold}")
    private double similarityThreshold;

    /**
     * 일반 텍스트 질문에 대해 AI 응답을 생성합니다.
     */
    public String generate(String userInput, String conversationId) {
        return buildPrompt(userInput, conversationId, List.of(loggerAdvisor, memoryAdvisor))
                .call()
                .content();
    }

    /**
     * RAG 기반 질문에 대해 벡터 검색을 포함한 AI 응답을 생성합니다.
     */
    public String ragGenerate(String userInput, String conversationId, Map<String, Object> advisorParams) {
        List<Advisor> advisors = new ArrayList<>(List.of(loggerAdvisor, memoryAdvisor));

        if (advisorParams != null && !advisorParams.isEmpty()) {
            var searchRequest = SearchRequest.builder()
                    .query(userInput)
                    .topK(topK)
                    .similarityThreshold(similarityThreshold)
                    .build();

            var template = PromptTemplate.builder()
                    .template(ragPrompt)
                    .variables(Map.of("userInput", userInput))
                    .build();

            var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                    .searchRequest(searchRequest)
                    .promptTemplate(template)
                    .build();

            advisors.add(qaAdvisor);
        }

        return buildPrompt(userInput, conversationId, advisors)
                .call()
                .content();
    }

    /**
     * ChatClientRequestSpec 공통 생성 메서드.
     */
    private ChatClient.ChatClientRequestSpec buildPrompt(String userInput, String conversationId, List<Advisor> advisors) {
        return chatClient.prompt()
                .system(s -> s
                        .param("language", "Korean")
                        .param("character", "챗봇"))
                .advisors(a -> a
                        .param(ChatMemory.CONVERSATION_ID, conversationId)
                        .advisors(advisors))
                .user(userInput);
    }
}
