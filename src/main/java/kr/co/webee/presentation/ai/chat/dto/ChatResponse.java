package kr.co.webee.presentation.ai.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI 응답 결과 객체")
public record ChatResponse(

        @Schema(description = "AI가 생성한 응답 텍스트", example = "호박벌은 온실 작물의 자가 수분에 효과적입니다.")
        String content,

        @Schema(description = "대화 식별자 (세션 ID)", example = "8a12f9c1-54a3-4d8f-b2c2-017fa38a7763")
        String conversationId
) {
}
