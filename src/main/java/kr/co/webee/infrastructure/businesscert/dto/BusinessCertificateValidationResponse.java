package kr.co.webee.infrastructure.businesscert.dto;

import java.util.List;

public record BusinessCertificateValidationResponse(
    List<ValidationInfo> data
) {
    public record ValidationInfo(
        String valid
    ){
    }
}

