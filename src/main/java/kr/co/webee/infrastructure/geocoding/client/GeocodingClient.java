package kr.co.webee.infrastructure.geocoding.client;

import kr.co.webee.domain.profile.crop.entity.Coordinates;

public interface GeocodingClient{

    Coordinates getCoordinateFrom(String address);
}
