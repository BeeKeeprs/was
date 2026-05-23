package kr.co.webee.domain.hive.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ControlMode {
    AUTO("자동 제어"),
    MANUAL("수동 제어");

    private final String description;
}
