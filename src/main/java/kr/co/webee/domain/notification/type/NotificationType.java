package kr.co.webee.domain.notification.type;

import kr.co.webee.domain.hive.type.SensorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    HIVE_ALERT("벌통 이상 상태 알림"),
    GATE_TUNNEL_ALERT("개폐기 통로 센서 이상 알림");

    private final String description;

    public String buildHiveAlertTitle(String hiveName, SensorType sensor) {
        return "[%s] 벌통 %s 이상 감지".formatted(hiveName, sensor.getDescription());
    }

    public String buildHiveAlertContent(String hiveName, SensorType sensor, Double value, Double threshold) {
        return "[%s] %s가 %.1f로 임계값 %.1f를 초과했습니다.".formatted(hiveName, sensor.getDescription(), value, threshold);
    }

    public String buildGateTunnelAlertTitle(String gateName) {
        return "[%s] 통로 센서 이상 감지".formatted(gateName);
    }

    public String buildGateTunnelAlertContent(String gateName, String tunnel, int sensor, int blockedSeconds) {
        String tunnelLabel = "entrance".equals(tunnel) ? "입구" : "출구";
        return "[%s] %s 동굴 %d번 센서가 %d초 동안 지속 감지됐습니다.".formatted(gateName, tunnelLabel, sensor, blockedSeconds);
    }
}
