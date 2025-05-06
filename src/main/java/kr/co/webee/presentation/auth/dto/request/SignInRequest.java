package kr.co.webee.presentation.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 request")
public record SignInRequest(
        @Schema(description = "아이디", example = "exampleId")
        @NotBlank(message = "아이디는 필수 항목입니다.")
        String username,

        @Schema(description = "비밀번호", example = "examplePassword")
        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        String password
) {
}
