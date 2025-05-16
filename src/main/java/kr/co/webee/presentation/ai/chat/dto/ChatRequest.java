package kr.co.webee.presentation.ai.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 질문 요청 객체")
public record ChatRequest(

        @Schema(description = "사용자의 입력 텍스트", example = "오이 수분 시기는 언제가 좋나요?")
        String text,

        @Schema(description = "대화 ID (null 가능), 처음에는 공백이나 null로 보내면 새로운 대화를 시작합니다.", nullable = true, example = "8a12f9c1-54a3-4d8f-b2c2-017fa38a7763")
        String conversationId
) {
}
