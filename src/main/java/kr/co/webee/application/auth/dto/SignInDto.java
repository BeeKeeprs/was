package kr.co.webee.application.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInDto(
        @NotBlank(message = "아이디는 필수 항목입니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        String password
) {
}
