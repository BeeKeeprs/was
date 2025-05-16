package kr.co.webee.presentation.ai.chat.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.presentation.ai.chat.dto.ChatRequest;
import kr.co.webee.presentation.ai.chat.dto.ChatResponse;

import java.util.List;

@Tag(name = "챗봇 API", description = "질문 응답 API")
public interface ChatBotApi {

    @Operation(summary = "FAQ 예시 질문 목록 조회", description = "벡터 DB에 저장된 벌 관련 FAQ 예시 질문들을 조회합니다.")
    List<String> getBeeFaqQuestions();

    @Operation(summary = "사용자 질문에 대한 AI 응답 생성", description = "사용자 질문에 대해 AI가 응답을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ChatResponse.class)))
    ChatResponse answerUserQuestion(ChatRequest request);
}
