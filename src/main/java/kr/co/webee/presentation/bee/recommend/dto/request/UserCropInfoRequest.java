package kr.co.webee.presentation.bee.recommend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.profile.crop.type.CultivationType;

import java.time.LocalDate;

@Schema(description = "사용자 재배 작물 정보 request")
public record UserCropInfoRequest(
        @Schema(description = "작물명", example = "딸기")
        @NotBlank
        String name,

        @Schema(description = "품종", example = "설향")
        @NotBlank
        String variety,

        @Schema(description = "재배 방식", example = "CONTROLLED", examples = {"CONTROLLED", "OPEN_FIELD"})
        @NotNull
        CultivationType cultivationType,

        @Schema(description = "재배 지역", example = "충청남도 논산시 연무읍 봉동리")
        @NotBlank
        String cultivationAddress,

        @Schema(description = "재배 면적", example = "1320")
        @NotNull
        Integer cultivationArea,

        @Schema(description = "정식(또는 파종)일", example = "2024-02-25")
        @NotNull
        LocalDate plantingDate
) {
    public String describe() {
        return String.format("작물명: %s, 품종: %s, 재배 방식: %s, 재배 지역: %s, 재배 면적: %s, 정식(또는 파종)일: %s",
                this.name, this.variety, this.cultivationType.getDescription(), this.cultivationAddress, this.cultivationArea, this.plantingDate.toString());
    }
}
