package kr.co.webee.presentation.interestpesticide.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.interestpesticide.entity.InterestPesticide;
import lombok.Builder;

@Builder
@Schema(description = "관심 농약 정보")
public record InterestPesticideListResponse(
        @Schema(description = "관심 농약 ID", example = "1")
        Long interestPesticideId,

        @Schema(description = "농약 등록번호", example = "1-1-000001")
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
    public static InterestPesticideListResponse from(InterestPesticide interestPesticide) {
        return InterestPesticideListResponse.builder()
                .interestPesticideId(interestPesticide.getId())
                .pesticideApplicationNo(interestPesticide.getPesticideApplicationNo())
                .brandName(interestPesticide.getBrandName())
                .productName(interestPesticide.getProductName())
                .contentInfo(interestPesticide.getContentInfo())
                .safeSprayInterval(interestPesticide.getSafeSprayInterval())
                .cropName(interestPesticide.getCropName())
                .insectName(interestPesticide.getInsectName())
                .usageName(interestPesticide.getUsageName())
                .targetPestName(interestPesticide.getTargetPestName())
                .build();
    }
}
