package kr.co.webee.presentation.hive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "벌통 교체 기록 등록 response")
public record HiveReplacementHistoryCreateResponse(
        @Schema(description = "교체 기록 ID", example = "31")
        Long replacementHistoryId
) {
    public static HiveReplacementHistoryCreateResponse of(Long replacementHistoryId) {
        return HiveReplacementHistoryCreateResponse.builder()
                .replacementHistoryId(replacementHistoryId)
                .build();
    }
}
