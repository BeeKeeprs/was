package kr.co.webee.presentation.auth.dto.response;

import lombok.Builder;

@Builder
public record SmsVerificationVerifyResponse(
        boolean verified
) {
    public static SmsVerificationVerifyResponse of(boolean verified) {
        return SmsVerificationVerifyResponse.builder()
                .verified(verified)
                .build();
    }
}
