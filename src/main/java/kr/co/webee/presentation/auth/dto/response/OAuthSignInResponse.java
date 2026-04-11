package kr.co.webee.presentation.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "소셜 로그인 response")
public record OAuthSignInResponse(
        @Schema(description = "신규 사용자 여부", example = "true")
        boolean isNewUser,

        @Schema(description = "사용자 이름", example = "홍길동")
        String name
) {
    public static OAuthSignInResponse of(boolean isNewUser, String name) {
        return OAuthSignInResponse.builder()
                .isNewUser(isNewUser)
                .name(name)
                .build();
    }
}
