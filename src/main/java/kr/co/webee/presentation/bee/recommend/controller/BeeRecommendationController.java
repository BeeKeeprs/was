package kr.co.webee.presentation.bee.recommend.controller;

import kr.co.webee.application.bee.recommend.service.BeeRecommendationService;
import kr.co.webee.infrastructure.config.ai.dto.BeeRecommendationAiResponse;
import kr.co.webee.presentation.bee.recommend.api.BeeRecommendationApi;
import kr.co.webee.presentation.bee.recommend.dto.request.UserCropInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bee/recommendations")
public class BeeRecommendationController implements BeeRecommendationApi {
    private final BeeRecommendationService beeRecommendationService;

    @Override
    @PostMapping("")
    public BeeRecommendationAiResponse recommendBee(@RequestBody UserCropInfoRequest request) {
        return beeRecommendationService.recommendBee(request);
    }
}
