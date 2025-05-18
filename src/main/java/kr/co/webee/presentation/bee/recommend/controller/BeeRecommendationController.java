package kr.co.webee.presentation.bee.recommend.controller;

import kr.co.webee.application.bee.recommend.service.BeeRecommendationService;
import kr.co.webee.infrastructure.config.ai.dto.BeeRecommendationAiResponse;
import kr.co.webee.presentation.annotation.UserId;
import kr.co.webee.presentation.bee.recommend.api.BeeRecommendationApi;
import kr.co.webee.presentation.bee.recommend.dto.request.BeeRecommendationRequest;
import kr.co.webee.presentation.bee.recommend.dto.request.UserCropInfoRequest;
import kr.co.webee.presentation.bee.recommend.dto.response.BeeRecommendationCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bee/recommendations")
public class BeeRecommendationController implements BeeRecommendationApi {
    private final BeeRecommendationService beeRecommendationService;

    @Override
    @PostMapping("/ai")
    public BeeRecommendationAiResponse recommendBee(@RequestBody UserCropInfoRequest request) {
        return beeRecommendationService.recommendBee(request);
    }

    @Override
    @PostMapping("/")
    public BeeRecommendationCreateResponse createBeeRecommendation(@RequestBody BeeRecommendationRequest request, @UserId Long userId) {
        return beeRecommendationService.createBeeRecommendation(request, userId);
    }

}
