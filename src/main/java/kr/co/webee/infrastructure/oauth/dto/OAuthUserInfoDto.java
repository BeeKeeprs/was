package kr.co.webee.infrastructure.oauth.dto;

import lombok.Builder;

@Builder
public record OAuthUserInfoDto(
        String platformId,
        String name // TODO: 추후 모든 name -> nickname으로 수정
){
    public static OAuthUserInfoDto of(String platformId, String name) {
        return OAuthUserInfoDto.builder()
                .platformId(platformId)
                .name(name)
                .build();
    }
}
