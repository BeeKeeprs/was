package kr.co.webee.presentation.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(description = "SMS 인증번호 확인 request")
public record SmsVerificationVerifyRequest(
        @Schema(description = "전화번호", example = "01012345678")
        @Pattern(
                regexp = "^010\\d{8}$",
                message = "전화번호 형식이 올바르지 않습니다."
        )
        @NotBlank
        String phoneNumber,

        @Schema(description = "인증번호", example = "133838")
        @Pattern(regexp = "^\\d{6}$", message = "인증번호 형식이 올바르지 않습니다.")
        @NotBlank
        String authCode
) {
}
