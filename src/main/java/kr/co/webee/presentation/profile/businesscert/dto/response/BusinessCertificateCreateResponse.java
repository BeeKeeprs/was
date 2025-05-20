package kr.co.webee.presentation.profile.businesscert.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "사업자등록정보 등록 response")
public record BusinessCertificateCreateResponse(
        @Schema(description = "사업자등록정보 ID", example = "1")
        Long businessCertificateId
) {
    public static BusinessCertificateCreateResponse of(Long businessCertificateId) {
        return BusinessCertificateCreateResponse.builder()
                .businessCertificateId(businessCertificateId)
                .build();
    }
}
