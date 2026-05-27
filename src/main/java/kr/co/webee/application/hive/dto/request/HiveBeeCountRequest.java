package kr.co.webee.application.hive.dto.request;

import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveBeeCount;

import java.time.LocalDateTime;

public record HiveBeeCountRequest(
        Integer count,
        LocalDateTime timestamp
) {
    public HiveBeeCount toEntity(Hive hive) {
        return HiveBeeCount.builder()
                .count(count)
                .recordedAt(timestamp)
                .hive(hive)
                .build();
    }
}
