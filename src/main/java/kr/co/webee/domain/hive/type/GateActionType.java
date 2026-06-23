package kr.co.webee.domain.hive.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum GateActionType {
    OPEN_ONLY("열기만"),
    CLOSE_ONLY("닫기만"),
    OPEN_CLOSE("여닫기");

    private final String description;
}
