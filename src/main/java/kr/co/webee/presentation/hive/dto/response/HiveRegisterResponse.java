package kr.co.webee.presentation.hive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "벌통 등록 response")
public record HiveRegisterResponse(
        @Schema(description = "벌통 ID", example = "1")
        Long hiveId
) {
    public static HiveRegisterResponse of(Long hiveId) {
        return HiveRegisterResponse.builder()
                .hiveId(hiveId)
                .build();
    }
}
