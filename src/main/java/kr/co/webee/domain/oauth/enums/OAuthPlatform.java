package kr.co.webee.domain.oauth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthPlatform {
    KAKAO("카카오"),
    NAVER("네이버");

    private final String description;
}