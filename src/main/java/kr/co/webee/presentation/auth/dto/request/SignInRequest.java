package kr.co.webee.presentation.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "로그인 request")
public record SignInRequest(
        @Schema(description = "아이디", example = "exampleId")
        @NotBlank
        String username,

        @Schema(description = "비밀번호", example = "examplePassword")
        @NotBlank
        String password
) {
}
