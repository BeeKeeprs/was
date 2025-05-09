package kr.co.webee.infrastructure.geocoding.dto;

import lombok.Builder;

@Builder
public record CoordinatesDto(
        Double latitude,
        Double longitude
) {
    public static CoordinatesDto of(Double latitude, Double longitude) {
        return CoordinatesDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}

