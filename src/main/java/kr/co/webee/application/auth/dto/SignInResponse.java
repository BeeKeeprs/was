package kr.co.webee.application.auth.dto;

import lombok.Builder;

@Builder
public record SignInResponse(
        String name,
        JwtTokenDto jwtTokenDto
) {
    public static SignInResponse of(String name, JwtTokenDto jwtTokenDto) {
        return SignInResponse.builder()
                .name(name)
                .jwtTokenDto(jwtTokenDto)
                .build();
    }
}
