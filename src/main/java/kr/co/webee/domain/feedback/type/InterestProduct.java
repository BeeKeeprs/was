package kr.co.webee.domain.feedback.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum InterestProduct {
    SMART_HIVE("smart-hive"),
    GATE("gate"),
    APP("app"),
    ALL("all"),
    OTHER("other");

    private final String value;
}
