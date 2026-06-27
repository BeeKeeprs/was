package kr.co.webee.application.ai;

import kr.co.webee.infrastructure.ai.AiPromptExecutor;
import kr.co.webee.presentation.ai.chat.dto.AssistantResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.*;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final VectorStore vectorStore;
    private final AiPromptExecutor aiPromptExecutor;

    @Value("${app.ai.vector-store.topK}")
    private int topK;

    @Value("${app.ai.vector-store.similarity-threshold}")
    private double similarityThreshold;

    public AssistantResponse answerUserInput(String input, String conversationId) {
        String convId = StringUtils.isBlank(conversationId) ? UUID.randomUUID().toString() : conversationId;

        RagSearchOptions ragOptions = new RagSearchOptions(
                "type != 'faq' AND type != 'guide'",
                topK,
                similarityThreshold
        );

        ChatResponse chatResponse = aiPromptExecutor.run(input, builder ->
                builder.input(input)
                        .withLogger()
                        .withMemory(convId)
                        .withRag("assistant-prompt", ragOptions)
        );

        String answer = chatResponse.getResult().getOutput().getText();

        List<Document> retrievedDocs = (List<Document>) chatResponse.getMetadata()
                .get(RETRIEVED_DOCUMENTS);

        List<String> sources = retrievedDocs == null ? List.of() : retrievedDocs.stream()
                .map(doc -> (String) doc.getMetadata().get("origin"))
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return new AssistantResponse(answer, convId, sources);
    }

    public List<String> getBeeFaqQuestions() {
        SearchRequest searchRequest = SearchRequest.builder()
                .query("bee faq")
                .topK(topK)
                .similarityThreshold(similarityThreshold)
                .filterExpression("type == 'faq' AND category == 'bee'")
                .build();

        List<Document> docs = vectorStore.similaritySearch(searchRequest);
        if (docs == null || docs.isEmpty()) return List.of();
        return docs.stream().map(Document::getText).toList();
    }
}
