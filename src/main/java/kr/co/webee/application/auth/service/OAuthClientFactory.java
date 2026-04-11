package kr.co.webee.application.auth.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.oauth.enums.OAuthPlatform;
import kr.co.webee.infrastructure.oauth.client.KakaoClient;
import kr.co.webee.infrastructure.oauth.client.NaverClient;
import kr.co.webee.infrastructure.oauth.client.OAuthClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class OAuthClientFactory {
    private final NaverClient naverClient;
    private final KakaoClient kakaoClient;

    public OAuthClient getOAuthClient(OAuthPlatform platform) {
        return switch (platform) {
            case NAVER -> naverClient;
            case KAKAO -> kakaoClient;
            default -> throw new BusinessException(ErrorType.UNSUPPORTED_SOCIAL_PLATFORM);
        };
    }
}