package kr.co.webee.presentation.bee.diagnosis.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.profile.crop.type.CultivationType;

@Schema(description = "꿀벌 질병 타입 및 재배 작물 정보 request")
public record BeeDiseaseAndUserCropInfoRequest(
        @Schema(description = "꿀벌 질병 타입", example = "성충 날개불구바이러스감염증")
        @NotBlank
        String disease,

        @Schema(description = "재배 방식", example = "CONTROLLED", examples = {"CONTROLLED", "OPEN_FIELD"})
        @NotNull
        CultivationType cultivationType,

        @Schema(description = "작물명", example = "딸기")
        @NotBlank
        String cropName,

        @Schema(description = "재배 지역", example = "충청남도 논산시 연무읍 봉동리")
        @NotBlank
        String cultivationAddress,

        @Schema(description = "추가 정보", example = "특이사항 또는 주변 환경 등")
        String details
) {
    public String describeDisease() {
        return String.format("질병 타입: %s,", this.disease);
    }

    public String describeUserCropInfo() {
        return String.format("작물명: %s, 재배 방식: %s, 재배 지역: %s, 추가 정보: %s",
                this.cropName, this.cultivationType.getDescription(), this.cultivationAddress, this.details);
    }
}
