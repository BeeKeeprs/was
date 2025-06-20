package kr.co.webee.application.bee.recommendation.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.application.ai.RagSearchOptions;
import kr.co.webee.domain.bee.recommendation.entity.BeeRecommendation;
import kr.co.webee.domain.bee.recommendation.repository.BeeRecommendationRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.ai.AiPromptExecutor;
import kr.co.webee.infrastructure.ai.PromptTemplateRegistry;
import kr.co.webee.presentation.bee.recommendation.dto.request.BeeRecommendationRequest;
import kr.co.webee.presentation.bee.recommendation.dto.request.UserCropInfoRequest;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationAiResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationCreateResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationDetailResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeeRecommendationService {
    private final BeeRecommendationRepository beeRecommendationRepository;
    private final UserRepository userRepository;
    private final AiPromptExecutor aiPromptExecutor;
    private final PromptTemplateRegistry promptRegistry;

    public BeeRecommendationAiResponse recommendBee(UserCropInfoRequest request) {
        String userInput = request.describe();

        String query = promptRegistry.get("bee-recommendation-query")
                .replace("{user_crop_info}", userInput);

        RagSearchOptions options = new RagSearchOptions(
                "type == 'guide' AND category == 'bee' OR category == 'crop'",
                5,
                0.75
        );

        return aiPromptExecutor.run(query, builder ->
                        builder.input(query)
                                .withRag("bee-recommendation-prompt", options),
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
