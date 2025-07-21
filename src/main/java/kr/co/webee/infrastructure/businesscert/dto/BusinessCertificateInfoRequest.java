package kr.co.webee.infrastructure.businesscert.dto;

import kr.co.webee.presentation.profile.business.dto.request.BusinessCreateRequest;
import lombok.Builder;

import java.time.LocalDate;


@Builder
public record BusinessCertificateInfoRequest(
        String b_no,
        String start_dt,
        String p_nm
) {
    public static BusinessCertificateInfoRequest from(BusinessCreateRequest request) {
        LocalDate commencementDate = request.commencementDate();

        String formattedDate = String.format("%04d%02d%02d",
                commencementDate.getYear(),
                commencementDate.getMonthValue(),
                commencementDate.getDayOfMonth());

        return BusinessCertificateInfoRequest.builder()
                .b_no(request.registrationNumber())
                .start_dt(formattedDate)
                .p_nm(request.representativeName())
                .build();
    }
}



