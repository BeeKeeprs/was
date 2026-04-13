package kr.co.webee.infrastructure.oauth.client;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.infrastructure.oauth.dto.OAuthUserInfoDto;
import kr.co.webee.infrastructure.oauth.properties.OAuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class NaverClient implements OAuthClient{
    private final RestClient restClient;
    private final OAuthProperties.Platform properties;

    public NaverClient(OAuthProperties properties) {
        this.restClient = RestClient.create();
        this.properties = properties.getNaver();
    }

    @Override
    public String getAccessToken(String code) {
        String url = UriComponentsBuilder.fromUriString(properties.getTokenUri())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", properties.getClientId())
                .queryParam("client_secret", properties.getClientSecret())
                .queryParam("code", code)
                .toUriString();

        try {
            JsonNode root = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(JsonNode.class);

            return root.get("access_token").asText();
        } catch (Exception e) {
            log.error("네이버 소셜 서버로부터 액세스 토큰을 가져오는데 실패했습니다. {}", e.getMessage());
            throw new BusinessException(ErrorType.OAUTH_TOKEN_REQUEST_FAILED);
        }
    }

    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        try {
            JsonNode root = restClient.get()
                    .uri(properties.getUserInfoUri())
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .body(JsonNode.class);

            String platformId = root.path("response").get("id").asText();
            String nickname = root.path("response").get("nickname").asText();

            return OAuthUserInfoDto.of(platformId, nickname);
        } catch (Exception e) {
            log.error("네이버 소셜 사용자 정보를 가져오는데 실패했습니다. {}", e.getMessage());
            throw new BusinessException(ErrorType.OAUTH_USER_INFO_REQUEST_FAILED);
        }
    }
}
