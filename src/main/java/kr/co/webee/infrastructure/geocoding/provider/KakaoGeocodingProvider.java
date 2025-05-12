package kr.co.webee.infrastructure.geocoding.provider;

import kr.co.webee.domain.profile.crop.entity.Coordinates;
import kr.co.webee.infrastructure.geocoding.dto.KakaoGeocodingResponse;
import kr.co.webee.infrastructure.geocoding.dto.KakaoCoordResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoGeocodingProvider {
    private final String KAKAO_GEOCODING_API_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private final String AUTHORIZATION_HEADER_PREFIX = "KakaoAK ";
    private final String ADDRESS_QUERY_PARAMETER = "query";

    @Value("${kakao.api-key}")
    private String kakaoApiKey;

    public Coordinates getCoordinatesFrom(String address) {
        KakaoGeocodingResponse response = requestKakaoGeocodingApi(address);
        return extractCoordinatesFrom(response);
    }

    private Coordinates extractCoordinatesFrom(KakaoGeocodingResponse response) {
        if (response.kakaoCoordResponse().isEmpty()) {
            throw new IllegalArgumentException("해당 주소에 대한 정보가 없습니다.");
        }
        KakaoCoordResponse coordResponse = response.kakaoCoordResponse().get(0);

        return Coordinates.builder()
                .latitude(coordResponse.latitude())
                .longitude(coordResponse.longitude())
                .build();
    }

    private KakaoGeocodingResponse requestKakaoGeocodingApi(String address) {
        WebClient webClient = WebClient.builder()
                .baseUrl(KAKAO_GEOCODING_API_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + kakaoApiKey)
                .build();

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(ADDRESS_QUERY_PARAMETER, address)
                        .build())
                .retrieve()
                .bodyToMono(KakaoGeocodingResponse.class)
                .block();
    }
}
