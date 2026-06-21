package kr.co.webee.presentation.hive.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.entity.HiveReplacementHistory;
import lombok.Builder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Builder
@Schema(description = "벌통 교체 기록")
public record HiveReplacementHistoryListResponse(
        @Schema(description = "교체 기록 ID", example = "31")
        Long replacementHistoryId,

        @Schema(description = "교체 일자", example = "2026-06-01")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate replacedAt,

        @Schema(description = "사용 일수", example = "20")
        long usageDays
) {
    public static HiveReplacementHistoryListResponse from(HiveReplacementHistory history) {
        long usageDays = history.getUsageDays() != null
                ? history.getUsageDays()
                : ChronoUnit.DAYS.between(history.getReplacedAt(), LocalDate.now());

        return HiveReplacementHistoryListResponse.builder()
                .replacementHistoryId(history.getId())
                .replacedAt(history.getReplacedAt())
                .usageDays(usageDays)
                .build();
    }
}
