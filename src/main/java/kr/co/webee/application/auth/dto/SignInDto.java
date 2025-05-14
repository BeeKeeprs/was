package kr.co.webee.application.auth.dto;

import lombok.Builder;

@Builder
public record SignInDto(
        String name,
        JwtTokenDto jwtTokenDto
) {
    public static SignInDto of(String name, JwtTokenDto jwtTokenDto) {
        return SignInDto.builder()
                .name(name)
                .jwtTokenDto(jwtTokenDto)
                .build();
    }
}
