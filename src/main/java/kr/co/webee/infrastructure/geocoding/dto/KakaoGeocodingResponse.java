package kr.co.webee.infrastructure.geocoding.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KakaoGeocodingResponse(
        @JsonProperty("documents") List<KakaoCoordResponse> kakaoCoordResponse
) {
}
