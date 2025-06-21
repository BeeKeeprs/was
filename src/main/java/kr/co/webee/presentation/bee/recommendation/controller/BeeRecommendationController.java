package kr.co.webee.presentation.bee.recommendation.controller;

import kr.co.webee.application.bee.recommendation.service.BeeRecommendationService;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationAiResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.bee.recommendation.api.BeeRecommendationApi;
import kr.co.webee.presentation.bee.recommendation.dto.request.BeeRecommendationRequest;
import kr.co.webee.presentation.bee.recommendation.dto.request.UserCropInfoRequest;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationCreateResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationDetailResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("")
    public BeeRecommendationCreateResponse createBeeRecommendation(@RequestBody BeeRecommendationRequest request, @UserId Long userId) {
        return beeRecommendationService.createBeeRecommendation(request, userId);
    }

    @Override
    @GetMapping("")
    public List<BeeRecommendationListResponse> getBeeRecommendationList(@UserId Long userId) {
        return beeRecommendationService.getBeeRecommendationList(userId);
    }

    @Override
    @GetMapping("/{beeRecommendId}")
    public BeeRecommendationDetailResponse getBeeRecommendationDetail(@PathVariable Long beeRecommendId) {
        return beeRecommendationService.getBeeRecommendationDetail(beeRecommendId);
    }
}
