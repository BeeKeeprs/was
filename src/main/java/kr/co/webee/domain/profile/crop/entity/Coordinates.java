package kr.co.webee.domain.profile.crop.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Coordinates {
    private Double latitude;

    private Double longitude;

    @Builder
    public Coordinates(Double latitude, Double longitude) {
        this.latitude = Objects.requireNonNull(latitude, "latitude는 null이 될 수 없습니다.");
        this.longitude = Objects.requireNonNull(longitude, "longitude는 null이 될 수 없습니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinates that = (Coordinates) o;
        return Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
