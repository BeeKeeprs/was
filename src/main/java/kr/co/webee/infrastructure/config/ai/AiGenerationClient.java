package kr.co.webee.infrastructure.config.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.AdvisorSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
public class AiGenerationClient {

    private final ChatClient vanillaChatClient;
    private final ChatClient ragChatDefaultClient;
    private final ChatClient ragChatCustomClient;

    public AiGenerationClient(
            @Qualifier("vanillaChatClientWithMemory") ChatClient vanillaChatClient,
            @Qualifier("ragChatDefaultClient") ChatClient ragChatDefaultClient,
            @Qualifier("ragChatCustomClient") ChatClient ragChatCustomClient
    ) {
        this.vanillaChatClient = vanillaChatClient;
        this.ragChatDefaultClient = ragChatDefaultClient;
        this.ragChatCustomClient = ragChatCustomClient;
    }

    /**
     * 사용자 입력에 대해 AI 응답을 생성합니다.
     * <p>
     * 이 메서드는 사용자 입력을 바탕으로 AI가 답변을 생성합니다.
     *
     * @param userInput 사용자의 질문 또는 입력 텍스트
     * @return AI가 생성한 응답 텍스트
     */
    public String generate(String userInput, String conversationId) {
        return vanillaChatClient.prompt()
                .system(s -> s
                        .param("language", "Korean")
                        .param("character", "챗봇")
                )
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(userInput)
                .call()
                .content();
    }

    /**
     * 사용자 입력에 대해 RAG(Retrieval-Augmented Generation) 기반 응답을 생성합니다.
     * <p>
     * 이 메서드는 사용자 입력을 바탕으로 VectorStore에서 관련 문서를 검색하고,
     * 검색된 문서(context)를 기반으로 AI가 답변을 생성합니다.
     *
     * @param userInput     사용자의 질문 또는 입력 텍스트
     * @param contextOnly   true인 경우 검색된 문서(context)만으로 응답을 구성하며, AI의 자유 응답 없이 제한된 답변을 생성합니다.
     *                      false인 경우 context와 사용자 입력을 함께 활용하여 AI가 더 유연한 응답을 생성합니다.
     * @param advisorParams 문서 검색 시 사용할 필터 조건입니다.
     *                      예: Map.of(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type == 'faq'")
     * @return RAG 기반으로 생성된 최종 응답 텍스트
     * <p>
     * 예시:
     * ragGeneration("수정벌은 언제 투입하나요?", false, Map.of("type", "faq"));
     */
    public String ragGenerate(String userInput, String conversationId, boolean contextOnly, Map<String, Object> advisorParams) {
        ChatClient chatClient = contextOnly ? ragChatDefaultClient : ragChatCustomClient;

        ChatClientRequestSpec promptSpec = chatClient.prompt()
                .system(s -> s
                        .param("language", "Korean")
                        .param("character", "챗봇")
                )
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(userInput);

        // Consumer<AdvisorSpec> spec = a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "category=='text'");
        if (advisorParams != null && !advisorParams.isEmpty()) {
            Consumer<AdvisorSpec> advisorSpec = a -> a.params(advisorParams);
            promptSpec.advisors(advisorSpec);
        }

        return promptSpec.call().content();
    }
}
