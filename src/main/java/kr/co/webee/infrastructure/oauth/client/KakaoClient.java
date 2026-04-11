package kr.co.webee.infrastructure.oauth.client;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.infrastructure.oauth.dto.OAuthUserInfoDto;
import kr.co.webee.infrastructure.oauth.properties.OAuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KakaoClient implements OAuthClient {
    private final RestClient restClient;
    private final OAuthProperties.Platform properties;

    public KakaoClient(OAuthProperties properties) {
        this.restClient = RestClient.create();
        this.properties = properties.getKakao();
    }

    @Override
    public String getAccessToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.getClientId());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", code);
        body.add("client_secret", properties.getClientSecret());

        try {
            JsonNode root = restClient.post()
                    .uri(properties.getTokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);

            return root.get("access_token").asText();
        } catch (Exception e) {
            log.error("카카오 소셜 서버로부터 액세스 토큰을 가져오는데 실패했습니다. {}", e.getMessage());
            throw new BusinessException(ErrorType.OAUTH_TOKEN_REQUEST_FAILED);
        }
    }

    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        try {
            JsonNode root = restClient.post()
                    .uri(properties.getUserInfoUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .body(JsonNode.class);

            String platformId = root.get("id").asText();
            return OAuthUserInfoDto.of(platformId);
        } catch (Exception e) {
            log.error("카카오 소셜 사용자 정보를 가져오는데 실패했습니다. {}", e.getMessage());
            throw new BusinessException(ErrorType.OAUTH_USER_INFO_REQUEST_FAILED);
        }
    }
}
