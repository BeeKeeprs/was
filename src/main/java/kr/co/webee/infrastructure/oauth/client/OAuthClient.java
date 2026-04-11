package kr.co.webee.infrastructure.oauth.client;

import kr.co.webee.infrastructure.oauth.dto.OAuthUserInfoDto;

public interface OAuthClient {

    String getAccessToken(String code);

    OAuthUserInfoDto getUserInfo(String accessToken);
}
