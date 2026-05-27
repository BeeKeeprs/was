package kr.co.webee.application.hive.dto.response;

import kr.co.webee.domain.hive.entity.Hive;

import java.time.LocalDateTime;

public record HiveConnectionResponse(
        boolean isConnected,
        LocalDateTime lastConnectedAt
) {
    public static HiveConnectionResponse from(Hive hive) {
        return new HiveConnectionResponse(hive.isConnected(), hive.getLastConnectedAt());
    }
}
