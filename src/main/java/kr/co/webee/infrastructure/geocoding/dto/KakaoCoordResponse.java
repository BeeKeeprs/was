package kr.co.webee.infrastructure.geocoding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoCoordResponse(
        @JsonProperty("y") Double latitude,
        @JsonProperty("x") Double longitude
) {
}
