package kr.co.webee.application.bee.recommendation.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.application.document.VectorDocumentService;
import kr.co.webee.common.document.util.DocumentFormatter;
import kr.co.webee.domain.bee.recommendation.entity.BeeRecommendation;
import kr.co.webee.domain.bee.recommendation.repository.BeeRecommendationRepository;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.ai.AdvisorBuilder;
import kr.co.webee.infrastructure.ai.AiPromptExecutor;
import kr.co.webee.infrastructure.ai.PromptTemplateRegistry;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.client.NongsaroCropPollinationClient;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.dto.NongsaroCropPollinationDetailResponse;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.dto.NongsaroCropPollinationParamDto;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.service.NongsaroCropPollinationSearchService;
import kr.co.webee.presentation.bee.recommendation.dto.request.BeeRecommendationRequest;
import kr.co.webee.presentation.bee.recommendation.dto.request.UserCropInfoRequest;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationAiResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationCreateResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationDetailResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kr.co.webee.domain.bee.type.BeeType.values;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeeRecommendationService {
    private final BeeRecommendationRepository beeRecommendationRepository;
    private final UserRepository userRepository;
    private final AiPromptExecutor aiPromptExecutor;
    private final PromptTemplateRegistry promptRegistry;
    private final NongsaroCropPollinationClient cropPollinationClient;
    private final NongsaroCropPollinationSearchService cropPollinationSearchService;
    private final VectorDocumentService vectorDocumentService;

    public BeeRecommendationAiResponse recommendBee(UserCropInfoRequest request) {
        Optional<NongsaroCropPollinationParamDto> optionalRequestParam = cropPollinationSearchService.searchRequestParamBy(request.name(), request.variety());

        String userInput = request.describe();
        String query = promptRegistry.get("bee-recommendation-query")
                .replace("{user_crop_info}", userInput);

        if (optionalRequestParam.isPresent()) {
            NongsaroCropPollinationParamDto requestParam = optionalRequestParam.get();
            return getBeeRecommendationByNongsaro(requestParam, query);
        }

        return getBeeRecommendationByDocument(request, userInput);
    }

    private BeeRecommendationAiResponse getBeeRecommendationByNongsaro(NongsaroCropPollinationParamDto requestParam, String query) {
        NongsaroCropPollinationDetailResponse cropPollinationDetail = cropPollinationClient.getCropPollinationDetail(requestParam);

        StringBuilder context = new StringBuilder(cropPollinationDetail.content());
        cropPollinationDetail.beeTypes()
                .forEach(beeType -> context.append(beeType.description()).append(", "));

        String prompt = promptRegistry.get("bee-recommendation-prompt")
                .replace("{query}", query)
                .replace("{question_answer_context}", context);

        return aiPromptExecutor.run(prompt, AdvisorBuilder.Builder::withLogger,
                BeeRecommendationAiResponse.class
        );
    }

    private BeeRecommendationAiResponse getBeeRecommendationByDocument(UserCropInfoRequest request, String userInput) {
        String beeCandidatesContext = DocumentFormatter.formatString(
                "",
                Arrays.stream(values())
                        .map(bee -> vectorDocumentService.searchWithFormat(bee.getName(), "bee", null, 5))
                        .toList()
        );

        String cropContext = vectorDocumentService.searchWithFormat(request.name(), "crop", null, 5);

        String prompt = promptRegistry.get("bee-recommendation-prompt-kor")
                .replace("{query}", userInput)
                .replace("{question_answer_context}", beeCandidatesContext + "\n" + cropContext);

        return aiPromptExecutor.run(prompt, AdvisorBuilder.Builder::withLogger,
                BeeRecommendationAiResponse.class
        );
    }

    @Transactional
    public BeeRecommendationCreateResponse createBeeRecommendation(BeeRecommendationRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        BeeRecommendation beeRecommendation = request.toEntity(user);
        beeRecommendationRepository.save(beeRecommendation);
        return BeeRecommendationCreateResponse.of(beeRecommendation.getId());
    }

    @Transactional(readOnly = true)
    public List<BeeRecommendationListResponse> getBeeRecommendationList(Long userId) {
        return beeRecommendationRepository.findByUserId(userId).stream()
                .map(BeeRecommendationListResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BeeRecommendationDetailResponse getBeeRecommendationDetail(Long beeRecommendId) {
        BeeRecommendation beeRecommendation = beeRecommendationRepository.findById(beeRecommendId)
                .orElseThrow(() -> new EntityNotFoundException("Bee Recommendation not found"));

        return BeeRecommendationDetailResponse.from(beeRecommendation);
    }
}
