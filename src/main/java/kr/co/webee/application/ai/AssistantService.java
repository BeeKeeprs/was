package kr.co.webee.application.ai;

import kr.co.webee.infrastructure.config.ai.AssistantAiExecutor;
import kr.co.webee.presentation.ai.chat.dto.AssistantResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final AssistantAiExecutor aiExecutor;

    @Value("${app.ai.rag-prompt}")
    private String ragPrompt;

    @Value("${app.ai.vector-store.topK}")
    private int topK;

    @Value("${app.ai.vector-store.similarity-threshold}")
    private double similarityThreshold;

    public AssistantResponse answerUserInput(String input, String conversationId, GenerationMode mode) {
        if (StringUtils.isBlank(conversationId)) {
            conversationId = UUID.randomUUID().toString();
        }

        String result = switch (mode) {
            case SIMPLE -> aiExecutor.generateSimple(input, conversationId);
            case RAG -> aiExecutor.generateWithRag(input, conversationId, ragPrompt, getDefaultRagOptions());
        };

        return new AssistantResponse(result, conversationId);
    }

    public List<String> getBeeFaqQuestions() {
        return aiExecutor.getDocuments(
                "bee faq",
                new RagSearchOptions(
                        "type == 'faq' AND category == 'bee'",
                        topK,
                        similarityThreshold
                )
        );
    }

    private RagSearchOptions getDefaultRagOptions() {
        return new RagSearchOptions("type != 'faq'", topK, similarityThreshold);
    }

    public enum GenerationMode {
        SIMPLE,
        RAG
    }
}
