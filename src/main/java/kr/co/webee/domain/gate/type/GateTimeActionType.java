package kr.co.webee.domain.gate.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GateTimeActionType {
    OPEN_AT("열기 예약"),
    CLOSE_AT("닫기 예약"),
    WINDOW("여닫기"),
    ALTERNATE_24H("24시간 교대");

    private final String description;
}
