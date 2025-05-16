package kr.co.webee.application.ai;

import kr.co.webee.infrastructure.config.ai.AiGenerationClient;
import kr.co.webee.presentation.ai.chat.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiGenerationService {

    private final AiGenerationClient aiGenerationClient;
    private final VectorStore vectorStore;

    public List<String> getBeeFaqQuestions() {
        SearchRequest searchRequest = SearchRequest.builder()
                .query("bee faq")
                .topK(3)
                .filterExpression("type == 'faq' AND category == 'bee'")
                .build();

        List<Document> faqDocs = vectorStore.similaritySearch(searchRequest);

        return Optional.ofNullable(faqDocs).stream().flatMap(List::stream)
                .map(Document::getText)
                .toList();
    }

    public ChatResponse answerUserQuestion(String userQuestion, String conversationId) {
        if (StringUtils.isBlank(conversationId)) {
            conversationId = UUID.randomUUID().toString();
        }

        String content = aiGenerationClient.ragGenerate(
                userQuestion,
                conversationId,
                Map.of(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type != 'faq'")
        );

        return new ChatResponse(content, conversationId);
    }
}
