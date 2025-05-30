package kr.co.webee.presentation.bee.recommend.controller;

import kr.co.webee.application.bee.recommend.service.BeeRecommendationService;
import kr.co.webee.presentation.bee.recommend.dto.response.BeeRecommendationAiResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.bee.recommend.api.BeeRecommendationApi;
import kr.co.webee.presentation.bee.recommend.dto.request.BeeRecommendationRequest;
import kr.co.webee.presentation.bee.recommend.dto.request.UserCropInfoRequest;
import kr.co.webee.presentation.bee.recommend.dto.response.BeeRecommendationCreateResponse;
import kr.co.webee.presentation.bee.recommend.dto.response.BeeRecommendationDetailResponse;
import kr.co.webee.presentation.bee.recommend.dto.response.BeeRecommendationListResponse;
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
    public BeeRecommendationCreateResponse createBeeRecommendation(@RequestBody BeeRecommendationRequest request) {
        return beeRecommendationService.createBeeRecommendation(request);
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
