package kr.co.webee.application.ai;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.infrastructure.ai.AiPromptExecutor;
import kr.co.webee.presentation.ai.chat.dto.AssistantResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.ai.vectorstore.SearchRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS;

@IntegrationTest
class AssistantServiceTest {

    @Autowired
    private AssistantService assistantService;

    @MockitoBean
    private AiPromptExecutor aiPromptExecutor;

    @MockitoBean
    private VectorStore vectorStore;

    @Nested
    @DisplayName("AI 답변 생성")
    class AnswerUserInput {

        @Test
        @DisplayName("출처가 있는 경우 sources에 포함된다.")
        void answerUserInput_withSources() {
            //given
            Document doc1 = new Document("내용1", Map.of("origin", "농촌진흥청 양봉 매뉴얼"));
            Document doc2 = new Document("내용2", Map.of("origin", "국립농업과학원 수정벌 가이드"));
            ChatResponse chatResponse = mockChatResponse("AI 답변", List.of(doc1, doc2));
            when(aiPromptExecutor.run(any(), any())).thenReturn(chatResponse);

            //when
            AssistantResponse response = assistantService.answerUserInput("질문", "conv-id");

            //then
            assertThat(response.answer()).isEqualTo("AI 답변");
            assertThat(response.sources()).containsExactlyInAnyOrder(
                    "농촌진흥청 양봉 매뉴얼",
                    "국립농업과학원 수정벌 가이드"
            );
        }

        @Test
        @DisplayName("참고 문서가 없으면 sources는 빈 목록이다.")
        void answerUserInput_noSources() {
            //given
            ChatResponse chatResponse = mockChatResponse("AI 답변", null);
            when(aiPromptExecutor.run(any(), any())).thenReturn(chatResponse);

            //when
            AssistantResponse response = assistantService.answerUserInput("질문", "conv-id");

            //then
            assertThat(response.sources()).isEmpty();
        }

        @Test
        @DisplayName("origin이 없는 문서는 sources에서 제외된다.")
        void answerUserInput_filterNullOrigin() {
            //given
            Document docWithOrigin = new Document("내용1", Map.of("origin", "농촌진흥청 양봉 매뉴얼"));
            Document docWithoutOrigin = new Document("내용2", Map.of());
            ChatResponse chatResponse = mockChatResponse("AI 답변", List.of(docWithOrigin, docWithoutOrigin));
            when(aiPromptExecutor.run(any(), any())).thenReturn(chatResponse);

            //when
            AssistantResponse response = assistantService.answerUserInput("질문", "conv-id");

            //then
            assertThat(response.sources()).containsExactly("농촌진흥청 양봉 매뉴얼");
        }

        @Test
        @DisplayName("중복된 출처는 하나만 포함된다.")
        void answerUserInput_distinctSources() {
            //given
            Document doc1 = new Document("내용1", Map.of("origin", "농촌진흥청 양봉 매뉴얼"));
            Document doc2 = new Document("내용2", Map.of("origin", "농촌진흥청 양봉 매뉴얼"));
            ChatResponse chatResponse = mockChatResponse("AI 답변", List.of(doc1, doc2));
            when(aiPromptExecutor.run(any(), any())).thenReturn(chatResponse);

            //when
            AssistantResponse response = assistantService.answerUserInput("질문", "conv-id");

            //then
            assertThat(response.sources()).hasSize(1)
                    .containsExactly("농촌진흥청 양봉 매뉴얼");
        }

        @Test
        @DisplayName("conversationId가 비어있으면 새 UUID를 생성한다.")
        void answerUserInput_blankConversationId() {
            //given
            ChatResponse chatResponse = mockChatResponse("AI 답변", null);
            when(aiPromptExecutor.run(any(), any())).thenReturn(chatResponse);

            //when
            AssistantResponse response = assistantService.answerUserInput("질문", "");

            //then
            assertThat(response.conversationId()).isNotBlank();
        }

        @Test
        @DisplayName("conversationId가 있으면 그대로 반환한다.")
        void answerUserInput_existingConversationId() {
            //given
            String conversationId = "existing-conv-id";
            ChatResponse chatResponse = mockChatResponse("AI 답변", null);
            when(aiPromptExecutor.run(any(), any())).thenReturn(chatResponse);

            //when
            AssistantResponse response = assistantService.answerUserInput("질문", conversationId);

            //then
            assertThat(response.conversationId()).isEqualTo(conversationId);
        }

        private ChatResponse mockChatResponse(String answer, List<Document> retrievedDocs) {
            ChatResponse chatResponse = mock(ChatResponse.class);
            Generation generation = mock(Generation.class);
            AssistantMessage assistantMessage = mock(AssistantMessage.class);
            ChatResponseMetadata metadata = mock(ChatResponseMetadata.class);

            when(chatResponse.getResult()).thenReturn(generation);
            when(generation.getOutput()).thenReturn(assistantMessage);
            when(assistantMessage.getText()).thenReturn(answer);
            when(chatResponse.getMetadata()).thenReturn(metadata);
            when(metadata.get(RETRIEVED_DOCUMENTS)).thenReturn(retrievedDocs);

            return chatResponse;
        }
    }

    @Nested
    @DisplayName("꿀벌 FAQ 질문 조회")
    class GetBeeFaqQuestions {

        @Test
        @DisplayName("FAQ 문서가 있으면 텍스트 목록을 반환한다.")
        void getBeeFaqQuestions_withDocs() {
            //given
            Document doc1 = new Document("꿀벌 FAQ 질문1");
            Document doc2 = new Document("꿀벌 FAQ 질문2");
            when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of(doc1, doc2));

            //when
            List<String> result = assistantService.getBeeFaqQuestions();

            //then
            assertThat(result).containsExactly("꿀벌 FAQ 질문1", "꿀벌 FAQ 질문2");
        }

        @Test
        @DisplayName("FAQ 문서가 없으면 빈 목록을 반환한다.")
        void getBeeFaqQuestions_empty() {
            //given
            when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of());

            //when
            List<String> result = assistantService.getBeeFaqQuestions();

            //then
            assertThat(result).isEmpty();
        }
    }
}
