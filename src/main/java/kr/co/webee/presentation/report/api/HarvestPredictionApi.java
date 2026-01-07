package kr.co.webee.presentation.report.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.presentation.report.dto.HarvestPrediction;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "수확량 및 품질 예측 API", description = "수확량 및 품질 예측 관련 API")
public interface HarvestPredictionApi {
    @Operation(
            summary = "AI 기반 수확량 및 품질 예측",
            description = "사용자의 농지 정보, 하우스 정보, 벌 사용 정보, 수익 정보, 스마트팜 정보를 기반으로 AI가 수확량과 고품질 작물 생산 가능성을 예측합니다. 응답 시간은 평균 2~5초 이내입니다."
    )
    @ApiResponse(responseCode = "200", description = "성공")
    HarvestPrediction.Response getHarvestPredictionReports(@RequestBody HarvestPrediction.Request request);
}
