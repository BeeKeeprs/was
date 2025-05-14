package kr.co.webee.presentation.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "로그인 응답")
public record SignInResponse(
        @Schema(description = "사용자 이름", example = "홍길동")
        String name
) {
    public static SignInResponse of(String name) {
        return SignInResponse.builder()
                .name(name)
                .build();
    }
}
