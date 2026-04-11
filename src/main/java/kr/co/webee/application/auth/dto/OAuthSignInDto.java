package kr.co.webee.application.auth.dto;

import lombok.Builder;

@Builder
public record OAuthSignInDto(
        boolean isNewUser,
        String name,
        JwtTokenDto jwtTokenDto
) {
    public static OAuthSignInDto of(boolean isNewUser, String name, JwtTokenDto jwtTokenDto){
        return OAuthSignInDto.builder()
                .isNewUser(isNewUser)
                .name(name)
                .jwtTokenDto(jwtTokenDto)
                .build();
    }
}
