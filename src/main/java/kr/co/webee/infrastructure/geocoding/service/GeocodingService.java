package kr.co.webee.infrastructure.geocoding.service;

import kr.co.webee.infrastructure.geocoding.dto.CoordinatesDto;
import kr.co.webee.infrastructure.geocoding.provider.KakaoGeocodingProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class GeocodingService {
    private final KakaoGeocodingProvider kakaoGeocodingProvider;

    public CoordinatesDto searchCoordinatesFrom(String address) {
        if (!StringUtils.hasText(address)) {
            throw new IllegalArgumentException("address는 null이거나 빈 문자열이 될 수 없습니다.");
        }

        return kakaoGeocodingProvider.getCoordinatesFrom(address);
    }
}

