package kr.co.webee.presentation.ai.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "AI 응답 결과 객체")
public record AssistantResponse(

        @Schema(description = "AI가 생성한 응답 텍스트", example = "호박벌은 토마토, 파프리카, 딸기 등 온실작물의 자가수분에 효과적입니다.")
        String answer,

        @Schema(description = "대화 식별자 (세션 ID)", example = "8a12f9c1-54a3-4d8f-b2c2-017fa38a7763")
        String conversationId,

        @Schema(description = "답변 생성에 참고한 문서 출처 목록", example = "[\"농촌진흥청 양봉 매뉴얼\", \"국립농업과학원 수정벌 가이드\"]")
        List<String> sources
) {
}
