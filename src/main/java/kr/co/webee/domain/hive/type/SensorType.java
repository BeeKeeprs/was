package kr.co.webee.domain.hive.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SensorType {
    INTERNAL_TEMPERATURE("내부 온도"),
    EXTERNAL_TEMPERATURE("외부 온도"),
    INTERNAL_HUMIDITY("내부 습도"),
    EXTERNAL_HUMIDITY("외부 습도"),
    CO2("CO2 농도");

    private final String description;
}
