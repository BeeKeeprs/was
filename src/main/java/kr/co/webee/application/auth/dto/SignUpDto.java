package kr.co.webee.application.auth.dto;

import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
public record SignUpDto(
        @NotBlank(message = "아이디는 필수 항목입니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        String password,

        @NotBlank(message = "이름은 필수 항목입니다.")
        String name
) {
    public User toEntity(String encodedPassword) {
        return User
                .builder()
                .username(username)
                .password(encodedPassword)
                .name(name)
                .build();
    }
}