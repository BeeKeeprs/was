package kr.co.webee.domain.hive.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ControlType {
    TEMPERATURE("온도"),
    HUMIDITY("습도"),
    CO2("CO2"),
    FAN("팬"),
    HEATER("히터"),
    DOOR("문");

    private final String description;

    public static List<ControlType> autoControlTypes() {
        return Arrays.asList(values());
    }

    public static List<ControlType> manualControlTypes() {
        return List.of(FAN, HEATER, DOOR);
    }
}
