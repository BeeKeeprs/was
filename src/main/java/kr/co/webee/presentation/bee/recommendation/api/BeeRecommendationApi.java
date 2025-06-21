package kr.co.webee.presentation.bee.recommendation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationAiResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.bee.recommendation.dto.request.BeeRecommendationRequest;
import kr.co.webee.presentation.bee.recommendation.dto.request.UserCropInfoRequest;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationCreateResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationDetailResponse;
import kr.co.webee.presentation.bee.recommendation.dto.response.BeeRecommendationListResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "수정벌 추천 API", description = "수정벌 추천 관련 API")
public interface BeeRecommendationApi {
    @Operation(
            summary = "AI 기반 수정벌 종류 및 투입 시기 추천",
            description = "사용자의 작물 정보와 벡터 저장소에 저장된 공공데이터를 기반으로 AI가 수정벌 종류 및 투입 시기를 추천합니다. 응답 시간은 평균 1~3초 이내입니다."
    )
    @ApiResponse(responseCode = "200", description = "성공")
    BeeRecommendationAiResponse recommendBee(@RequestBody UserCropInfoRequest request);

    @Operation(
            summary = "수정벌 추천 내역 저장",
            description = "AI로부터 응답받은 수정벌 추천 내역을 저장합니다. 로그인한 사용자만 저장할 수 있습니다."
    )
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "404", description = "사용자 재배 작물 없음")
    BeeRecommendationCreateResponse createBeeRecommendation(@RequestBody BeeRecommendationRequest request, @UserId Long userId);

    @Operation(
            summary = "수정벌 추천 내역 목록 조회",
            description = "수정벌 추천 내역 목록을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "성공")
    List<BeeRecommendationListResponse> getBeeRecommendationList(@UserId Long userId);

    @Operation(
            summary = "수정벌 추천 내역 상세 조회",
            description = "수정벌 추천 내역을 상세 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "404", description = "수정벌 추천 내역 없음")
    BeeRecommendationDetailResponse getBeeRecommendationDetail(@PathVariable Long beeRecommendId);
}
