package kr.co.webee.presentation.report.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.presentation.report.dto.HarvestPrediction;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "수확량 및 품질 예측 API", description = "수확량 및 품질 예측 관련 API")
public interface HarvestPredictionApi {
    @Operation(
            summary = "AI 기반 스마트 농업 분석 리포트 생성",
            description = """
                    사용자의 농지 정보(위치/면적/작물/품종), 하우스 시설 정보(하우스 수/면적/벌 사용/연 생산량), 환경 데이터(온도/습도, 선택)를 기반으로 AI가 종합 분석 리포트를 생성합니다.
                    
                    **리포트 구성:**
                    1. 수정벌 관리 최적화 지수 (점수 및 상위 퍼센트)
                    2. 맞춤 수정벌 추천 (최적 벌 + 대안)
                    3. 온습도 상태 진단 (현재 상태 + 경고)
                    4. 우선 개선 사항 (심각도 순)
                    5. 관리 가이드 (온도/습도/수분 관리)
                    6. 기대 수익 분석 (추가 생산량 및 ROI)
                    7. 최종 결론 요약
                    
                    응답 시간은 평균 2~5초 이내입니다.
                    """
    )
    @ApiResponse(responseCode = "200", description = "성공")
    HarvestPrediction.Response getHarvestPredictionReports(@RequestBody HarvestPrediction.Request request);
}
