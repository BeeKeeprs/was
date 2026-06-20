package kr.co.webee.domain.hive.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ControlType {
    TEMPERATURE("온도"),
    HUMIDITY("습도"),
    FAN("팬"),
    HEATER("히터"),
    COOLER("냉각기"),
    CIRCULATION("환기");

    private final String description;

    public static List<ControlType> autoControlTypes() {
        return List.of(TEMPERATURE, HUMIDITY, FAN);
    }

    public static List<ControlType> manualControlTypes() {
        return List.of(HEATER, COOLER, FAN, CIRCULATION);
    }
}
