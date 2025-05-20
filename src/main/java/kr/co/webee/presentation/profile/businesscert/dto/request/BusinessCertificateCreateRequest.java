package kr.co.webee.presentation.profile.businesscert.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.profile.businesscert.entity.BusinessCertificate;
import kr.co.webee.domain.profile.crop.entity.Coordinates;
import kr.co.webee.domain.profile.crop.entity.Location;
import kr.co.webee.domain.user.entity.User;

import java.time.LocalDate;

public record BusinessCertificateCreateRequest(
        @Schema(description = "사업자등록번호", example = "1023456798")
        @NotNull
        String registrationNumber,

        @Schema(description = "대표자명", example = "홍길동")
        @NotNull
        String representativeName,

        @Schema(description = "개업 일자", example = "2020-03-15")
        @NotNull
        LocalDate commencementDate,

        @Schema(description = "상호명", example = "꿀벌 농장")
        @NotNull
        String companyName,

        @Schema(description = "사업장 소재지", example = "전라북도 전주시 완산구 홍산로")
        @NotNull
        String businessAddress,

        @Schema(description = "온라인 스토어 링크", example = "https://smartstore.naver.com/honeybee", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String onlineStoreUrl
) {
    public BusinessCertificate toEntity(Coordinates coordinates, User user) {
        Location businessLocation = Location.builder()
                .address(businessAddress)
                .coordinates(coordinates)
                .build();

        return BusinessCertificate.builder()
                .registrationNumber(registrationNumber)
                .representativeName(representativeName)
                .commencementDate(commencementDate)
                .companyName(companyName)
                .businessLocation(businessLocation)
                //.imageUrl()
                .onlineStoreUrl(onlineStoreUrl)
                .user(user)
                .build();
    }
}
