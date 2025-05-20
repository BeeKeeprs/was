package kr.co.webee.presentation.profile.businesscert.dto.response;

import lombok.Builder;

@Builder
public record BusinessCertificateCreateResponse(
        Long businessCertificateId
) {
    public static BusinessCertificateCreateResponse of(Long businessCertificateId) {
        return BusinessCertificateCreateResponse.builder()
                .businessCertificateId(businessCertificateId)
                .build();
    }
}
