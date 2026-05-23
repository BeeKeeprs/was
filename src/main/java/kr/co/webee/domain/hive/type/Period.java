package kr.co.webee.domain.hive.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Period {
    DAY("하루"),
    WEEK("일주일"),
    MONTH("한달");

    private final String description;
}
