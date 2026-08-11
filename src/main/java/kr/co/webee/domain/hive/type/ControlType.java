package kr.co.webee.domain.hive.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ControlType {
    TEMPERATURE("온도"),
    HUMIDITY("습도");

    private final String description;

    public static List<ControlType> autoControlTypes() {
        return List.of(TEMPERATURE, HUMIDITY);
    }
}
