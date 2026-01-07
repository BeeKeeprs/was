package kr.co.webee.application.report.service;

import kr.co.webee.application.ai.RagSearchOptions;
import kr.co.webee.application.report.util.PromptFormatter;
import kr.co.webee.infrastructure.ai.AiPromptExecutor;
import kr.co.webee.infrastructure.ai.PromptTemplateRegistry;
import kr.co.webee.presentation.report.dto.HarvestPrediction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HarvestPredictionService {
    private final AiPromptExecutor aiPromptExecutor;
    private final PromptTemplateRegistry promptRegistry;

    public HarvestPrediction.Response getHarvestPredictionReports(HarvestPrediction.Request request){
        String userInput = PromptFormatter.toPromptString(request);

        RagSearchOptions options = new RagSearchOptions(
                "type == 'fact' AND (category == 'crop' OR category == 'harvest' OR category == 'bee')",
                5,
                0.75
        );

        return aiPromptExecutor.run(userInput, builder ->
                        builder.input(userInput)
                                .withLogger()
                                .withRag("harvest-prediction-prompt", options),
                HarvestPrediction.Response.class
        );
    }
}
