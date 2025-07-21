package kr.co.webee.infrastructure.businesscert.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BusinessCertificateValidationRequest(
        List<BusinessCertificateInfoRequest> businesses
) {
    public static BusinessCertificateValidationRequest of(List<BusinessCertificateInfoRequest> requests) {
        return BusinessCertificateValidationRequest.builder()
                .businesses(requests)
                .build();
    }
}
