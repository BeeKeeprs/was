package kr.co.webee.application.bee.recommend.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.application.ai.RagSearchOptions;
import kr.co.webee.domain.bee.recommend.entity.BeeRecommendation;
import kr.co.webee.domain.bee.recommend.repository.BeeRecommendationRepository;
import kr.co.webee.domain.profile.crop.entity.UserCrop;
import kr.co.webee.domain.profile.crop.repository.UserCropRepository;
import kr.co.webee.infrastructure.config.ai.BeeRecommendationAiExecutor;
import kr.co.webee.infrastructure.config.ai.dto.BeeRecommendationAiResponse;
import kr.co.webee.presentation.bee.recommend.dto.request.BeeRecommendationRequest;
import kr.co.webee.presentation.bee.recommend.dto.request.UserCropInfoRequest;
import kr.co.webee.presentation.bee.recommend.dto.response.BeeRecommendationCreateResponse;
import kr.co.webee.presentation.bee.recommend.dto.response.BeeRecommendationListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BeeRecommendationService {
    private final BeeRecommendationAiExecutor aiExecutor;
    private final BeeRecommendationRepository beeRecommendationRepository;
    private final UserCropRepository userCropRepository;

    @Value("${app.ai.bee-recommendation-rag-prompt}")
    private Resource ragPromptResource;

    @Value("${app.ai.bee-recommendation-rag-query}")
    private Resource ragQueryResource;

    private String ragPrompt;

    private String ragQuery;

    @Value("${app.ai.vector-store.topK}")
    private int topK;

    @Value("${app.ai.vector-store.similarity-threshold}")
    private double similarityThreshold;

    @PostConstruct
    public void init() {
        try (InputStream promptIs = ragPromptResource.getInputStream();
             InputStream queryIs = ragQueryResource.getInputStream()) {

            this.ragPrompt = new String(promptIs.readAllBytes(), StandardCharsets.UTF_8);
            this.ragQuery = new String(queryIs.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load RAG prompt", e);
        }
    }

    public BeeRecommendationAiResponse recommendBee(UserCropInfoRequest request) {
        RagSearchOptions searchOptions = new RagSearchOptions(
                "type == 'guide' AND category == 'bee' OR category == 'crop'",
                topK,
                similarityThreshold
        );

        return aiExecutor.recommendBeeWithRag(request.describe(), ragPrompt, ragQuery, searchOptions);
    }

    public BeeRecommendationCreateResponse createBeeRecommendation(BeeRecommendationRequest request) {
        UserCrop userCrop = userCropRepository.findById(request.userCropId())
                .orElseThrow(() -> new EntityNotFoundException("user crop not found"));

        BeeRecommendation beeRecommendation = request.toEntity(userCrop);
        beeRecommendationRepository.save(beeRecommendation);
        return BeeRecommendationCreateResponse.of(beeRecommendation.getId());
    }

    public List<BeeRecommendationListResponse> getBeeRecommendationList(Long userId) {
        return beeRecommendationRepository.findByUserId(userId).stream()
                .map(BeeRecommendationListResponse::from)
                .toList();
    }
}
