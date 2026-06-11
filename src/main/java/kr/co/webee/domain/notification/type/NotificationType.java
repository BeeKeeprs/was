package kr.co.webee.domain.notification.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    HIVE_ALERT("벌통 이상 상태 알림");

    private final String description;
}
