package kr.co.webee.infrastructure.oauth.dto;

import lombok.Builder;

@Builder
public record OAuthUserInfoDto(
        String platformId
){
    public static OAuthUserInfoDto of(String platformId) {
        return OAuthUserInfoDto.builder()
                .platformId(platformId)
                .build();
    }
}
