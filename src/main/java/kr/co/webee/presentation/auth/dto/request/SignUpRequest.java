package kr.co.webee.presentation.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "회원가입 request")
public record SignUpRequest(
        @Schema(description = "아이디", example = "exampleId")
        @NotBlank()
        String username,

        @Schema(description = "비밀번호", example = "examplePassword")
        @NotBlank()
        String password,

        @Schema(description = "이름", example = "exampleName")
        @NotBlank()
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