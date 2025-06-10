package kr.co.webee.application.bee.diagnosis;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.application.ai.RagSearchOptions;
import kr.co.webee.domain.bee.diagnosis.entity.BeeDiagnosis;
import kr.co.webee.domain.bee.diagnosis.repository.BeeDiagnosisRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.ai.AiPromptExecutor;
import kr.co.webee.infrastructure.ai.PromptTemplateRegistry;
import kr.co.webee.infrastructure.bee.diagnosis.client.BeeDiseaseDetectClient;
import kr.co.webee.infrastructure.storage.FileStorageClient;
import kr.co.webee.presentation.bee.diagnosis.dto.BeeDiagnosisRequest;
import kr.co.webee.presentation.bee.diagnosis.dto.BeeDiseaseAiSolutionResponse;
import kr.co.webee.presentation.bee.diagnosis.dto.BeeDiseaseAndUserCropInfoRequest;
import kr.co.webee.infrastructure.bee.diagnosis.dto.BeeDiseaseDetectResultResponse;
import kr.co.webee.presentation.bee.diagnosis.dto.BeeDiagnosisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BeeDiagnosisService {
    private final BeeDiseaseDetectClient beeDiseaseDetectClient;
    private final FileStorageClient fileStorageClient;
    private final AiPromptExecutor aiPromptExecutor;
    private final PromptTemplateRegistry promptRegistry;
    private final UserRepository userRepository;
    private final BeeDiagnosisRepository beeDiagnosisRepository;

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

    @Transactional
    public BeeDiagnosisSaveResponse saveBeeDiagnosis(MultipartFile image, BeeDiagnosisRequest request, Long userId) {
        String prefix = "/bee-diagnosis" + userId;
        String imageUrl = fileStorageClient.upload(image, prefix);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        BeeDiagnosis beeDiagnosis = request.toEntity(imageUrl, user);
        beeDiagnosisRepository.save(beeDiagnosis);
        return BeeDiagnosisSaveResponse.of(beeDiagnosis.getId());
    }
}
