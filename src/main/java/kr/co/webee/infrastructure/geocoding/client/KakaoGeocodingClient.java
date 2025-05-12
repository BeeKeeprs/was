package kr.co.webee.infrastructure.geocoding.client;

import kr.co.webee.domain.profile.crop.entity.Coordinates;
import kr.co.webee.infrastructure.geocoding.dto.KakaoGeocodingResponse;
import kr.co.webee.infrastructure.geocoding.dto.KakaoCoordResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoGeocodingClient implements GeocodingClient {
    private final String KAKAO_GEOCODING_API_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private final String AUTHORIZATION_HEADER_PREFIX = "KakaoAK ";
    private final String ADDRESS_QUERY_PARAMETER = "query";

    private final RestClient restClient;

    public KakaoGeocodingClient(RestClient.Builder restClientBuilder,
                                @Value("${kakao.api-key}") String kakaoApiKey) {
        this.restClient = restClientBuilder
                .baseUrl(KAKAO_GEOCODING_API_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + kakaoApiKey)
                .build();
    }

    @Override
    public Coordinates getCoordinateFrom(String address) {
        KakaoGeocodingResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam(ADDRESS_QUERY_PARAMETER, address).build())
                .retrieve()
                .body(KakaoGeocodingResponse.class);

        if (response == null || response.kakaoCoordResponse().isEmpty()) {
            throw new IllegalArgumentException("해당 주소에 대한 정보가 없습니다.");
        }

        KakaoCoordResponse coord = response.kakaoCoordResponse().get(0);
        return new Coordinates(coord.latitude(), coord.longitude());
    }
}
