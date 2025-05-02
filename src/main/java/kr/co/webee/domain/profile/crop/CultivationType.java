package kr.co.webee.domain.profile.crop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CultivationType {

    CONTROLLED("시설 재배"),
    OPEN_FIELD("노지");

    private final String description;
}
