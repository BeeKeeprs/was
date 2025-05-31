package kr.co.webee.presentation.profile.business.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "업체 정보 등록 response")
public record BusinessCreateResponse(
        @Schema(description = "업체 ID", example = "1")
        Long businessId
) {
    public static BusinessCreateResponse of(Long businessId) {
        return BusinessCreateResponse.builder()
                .businessId(businessId)
                .build();
    }
}
