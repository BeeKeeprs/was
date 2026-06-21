package kr.co.webee.presentation.hive.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "벌통 교체 기록 수정 request")
public record HiveReplacementHistoryUpdateRequest(
        @Schema(description = "교체 일자", example = "2026-06-05")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull
        LocalDate replacedAt
) {
}
