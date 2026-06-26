package kr.co.webee.presentation.interestpesticide.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.interestpesticide.entity.InterestPesticide;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "관심 농약 등록 request")
public record InterestPesticideRegisterRequest(
        @Schema(description = "농약 등록번호", example = "1-1-000001")
        @NotBlank
        String pesticideApplicationNo,

        @Schema(description = "상표명", example = "유기농바이오킬")
        String brandName,

        @Schema(description = "품목명", example = "비티수화제")
        String productName,

        @Schema(description = "용량정보", example = "250ml")
        String contentInfo,

        @Schema(description = "안전사용기준일", example = "3일")
        String safeSprayInterval,

        @Schema(description = "작물명", example = "벼")
        String cropName,

        @Schema(description = "해충명", example = "벼멸구")
        String insectName,

        @Schema(description = "사용방법", example = "경엽처리")
        String usageName,

        @Schema(description = "적용대상 병해충명", example = "벼멸구, 흰등멸구")
        String targetPestName
) {
    public InterestPesticide toEntity(User user) {
        return InterestPesticide.builder()
                .user(user)
                .pesticideApplicationNo(pesticideApplicationNo)
                .brandName(brandName)
                .productName(productName)
                .contentInfo(contentInfo)
                .safeSprayInterval(safeSprayInterval)
                .cropName(cropName)
                .insectName(insectName)
                .usageName(usageName)
                .targetPestName(targetPestName)
                .build();
    }
}
