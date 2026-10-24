package kr.co.webee.presentation.feedback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.feedback.type.InterestProduct;
import lombok.Builder;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Schema(description = "문의 등록 request")
public record FeedbackSubmitRequest(
        @Schema(description = "이름", example = "홍길동", requiredMode = REQUIRED)
        @NotBlank
        String name,

        @Schema(description = "전화번호", example = "010-1234-5678", nullable = true)
        String phone,

        @Schema(description = "이메일", example = "user@example.com", nullable = true)
        @Email
        String email,

        @Schema(description = "소속", example = "위비", nullable = true)
        String organization,

        @Schema(description = "관심 제품", example = "SMART_HIVE", requiredMode = REQUIRED)
        @NotNull
        InterestProduct interestProduct,

        @Schema(description = "문의 내용", example = "스마트 벌통 도입을 문의합니다.", requiredMode = REQUIRED)
        @NotBlank
        String message
) {
}
