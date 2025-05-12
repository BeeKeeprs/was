package kr.co.webee.domain.profile.crop.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Location {
    private String address;

    @Embedded
    private Coordinates coordinates;

    @Builder
    public Location(String address, Coordinates coordinates) {
        if (!StringUtils.hasText(address)) {
            throw new IllegalArgumentException("address는 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.address = address;
        this.coordinates = Objects.requireNonNull(coordinates, "coordinates는 null이 될 수 없습니다.");
    }

    public boolean isSameAddress(String address) {
        return this.address.equals(address);
    }
}
