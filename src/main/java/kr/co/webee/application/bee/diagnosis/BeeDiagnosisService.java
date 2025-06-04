package kr.co.webee.application.bee.diagnosis;

import kr.co.webee.application.ai.RagSearchOptions;
import kr.co.webee.infrastructure.ai.AiPromptExecutor;
import kr.co.webee.infrastructure.ai.PromptTemplateRegistry;
import kr.co.webee.infrastructure.bee.diagnosis.client.BeeDiseaseDetectClient;
import kr.co.webee.presentation.bee.diagnosis.dto.BeeDiseaseAiSolutionResponse;
import kr.co.webee.presentation.bee.diagnosis.dto.BeeDiseaseAndUserCropInfoRequest;
import kr.co.webee.infrastructure.bee.diagnosis.dto.BeeDiseaseDetectResultResponse;
import kr.co.webee.presentation.bee.diagnosis.dto.BeeDiagnosisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BeeDiagnosisService {
    private final BeeDiseaseDetectClient beeDiseaseDetectClient;
    private final AiPromptExecutor aiPromptExecutor;
    private final PromptTemplateRegistry promptRegistry;

    public BeeDiagnosisResponse diagnoseBeeDisease(MultipartFile image) {
        BeeDiseaseDetectResultResponse.Prediction prediction = beeDiseaseDetectClient.detect(image);
        return BeeDiagnosisResponse.from(prediction.type(), prediction.confidence());
    }

    public BeeDiseaseAiSolutionResponse getBeeDiseaseAiCustomSolution(BeeDiseaseAndUserCropInfoRequest request) {
        String query = promptRegistry.get("bee-disease-solution-query")
                .replace("{disease}", request.describeDisease())
                .replace("{user_crop_info}", request.describeUserCropInfo());

        RagSearchOptions options = new RagSearchOptions(
                "type == 'fact' AND category == 'bee' AND category == 'disease'",
                5,
                0.75
        );

        return aiPromptExecutor.run(query, builder ->
                builder.input(query)
                        .withRag("bee-disease-solution-prompt", options),
                BeeDiseaseAiSolutionResponse.class
        );
    }
}
