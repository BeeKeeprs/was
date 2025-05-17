package kr.co.webee.presentation.ai.chat.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.presentation.ai.chat.dto.AssistantRequest;
import kr.co.webee.presentation.ai.chat.dto.AssistantResponse;

import java.util.List;

@Tag(name = "챗봇 API", description = "질문 응답 API")
public interface AssistantApi {
    @Operation(
            summary = "AI 기반 질문 응답 생성",
            description = "사용자의 질문에 대해 AI가 실시간으로 응답을 생성합니다. 응답 시간은 평균 1~3초 이내입니다."
    )
    @ApiResponse(responseCode = "200", description = "성공")
    AssistantResponse answerUserInput(AssistantRequest request);

    @Operation(summary = "FAQ 예시 질문 목록 조회", description = "벡터 DB에 저장된 벌 관련 FAQ 예시 질문들을 조회합니다.")
    List<String> getBeeFaqQuestions();
}
