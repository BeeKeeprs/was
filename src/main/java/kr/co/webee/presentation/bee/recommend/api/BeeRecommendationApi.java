package kr.co.webee.presentation.bee.recommend.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.infrastructure.config.ai.dto.BeeRecommendationAiResponse;
import kr.co.webee.presentation.bee.recommend.dto.request.UserCropInfoRequest;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "수정벌 추천 API", description = "수정벌 추천 관련 API")
public interface BeeRecommendationApi {
    @Operation(
            summary = "AI 기반 수정벌 종류 및 투입 시기 추천",
            description = "사용자의 작물 정보와 벡터 저장소에 저장된 공공데이터를 기반으로 AI가 수정벌 종류 및 투입 시기를 추천합니다. 응답 시간은 평균 1~3초 이내입니다."
    )
    @ApiResponse(responseCode = "200", description = "성공")
    BeeRecommendationAiResponse recommendBee(@RequestBody UserCropInfoRequest request);
}
