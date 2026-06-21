package kr.co.webee.presentation.hive.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveReplacementHistory;

import java.time.LocalDate;

@Schema(description = "벌통 교체 기록 등록 request")
public record HiveReplacementHistoryCreateRequest(
        @Schema(description = "교체 일자", example = "2026-06-01")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull
        @PastOrPresent
        LocalDate replacedAt
) {
    public HiveReplacementHistory toEntity(Hive hive) {
        return HiveReplacementHistory.builder()
                .hive(hive)
                .replacedAt(replacedAt)
                .build();
    }
}
