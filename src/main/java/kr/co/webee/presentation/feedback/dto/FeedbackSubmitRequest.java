package kr.co.webee.presentation.feedback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "사용자 피드백 제안 request")
public record FeedbackSubmitRequest(
        @Schema(description = "내용", example = "수정벌 추천 기능에 대해 건의 드립니다. ...")
        @NotBlank
        String content
) {
}
