package kr.co.webee.presentation.ai.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.application.ai.AssistantService;

@Schema(description = "사용자 입력 요청 객체")
public record AssistantRequest(

        @Schema(description = "사용자의 입력 내용 (질문, 요청, 명령 등)", example = "호박벌은 어디에 쓰이죠?")
        String input,

        @Schema(description = "대화 ID (null 가능). null이면 새 대화를 시작합니다.", nullable = true, example = "8a12f9c1-54a3-4d8f-b2c2-017fa38a7763")
        String conversationId,

        @Schema(description = "SIMPLE 모드, RAG 모드 중 선택", example = "RAG", defaultValue = "RAG")
        AssistantService.GenerationMode mode
) {
}
