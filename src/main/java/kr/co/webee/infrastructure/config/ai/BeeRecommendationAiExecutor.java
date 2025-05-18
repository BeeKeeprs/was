package kr.co.webee.infrastructure.config.ai;

import kr.co.webee.application.ai.RagSearchOptions;
import kr.co.webee.infrastructure.config.ai.dto.BeeRecommendationAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BeeRecommendationAiExecutor {
    private final AiPromptHelper aiPromptHelper;

    public BeeRecommendationAiResponse recommendBeeWithRag(String userCropInfo, String prompt, String query, RagSearchOptions options) {
        String renderedQuery = query.replace("{user_crop_info}", userCropInfo);
        QuestionAnswerAdvisor qaAdvisor = aiPromptHelper.buildQaAdvisor(query, options, prompt);

        return aiPromptHelper.buildStatelessPrompt(renderedQuery, List.of(qaAdvisor))
                .call().entity(BeeRecommendationAiResponse.class);
    }
}
