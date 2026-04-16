package kr.co.webee.presentation.market.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.market.entity.InterestMarket;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "관심 시장 등록 request")
public record InterestMarketRegisterRequest(
        @Schema(description = "시세 API 기준 시장 코드", example = "110001")
        @NotBlank
        String marketCode,

        @Schema(description = "작물 대분류 코드", example = "01")
        String cropMajorCode,

        @Schema(description = "작물 중분류 이름", example = "딸기")
        String cropMidName,

        @Schema(description = "작물 소분류 이름", example = "설향")
        String cropMinorName
) {
    public InterestMarket toEntity(User user) {
        return InterestMarket.builder()
                .marketCode(marketCode)
                .cropMajorCode(cropMajorCode)
                .cropMidName(cropMidName)
                .cropMinorName(cropMinorName)
                .user(user)
                .build();
    }

    @JsonIgnore
    public boolean isMarketOnly() {
        return (cropMajorCode == null) && (cropMidName == null) && (cropMinorName == null);
    }

    @JsonIgnore
    public boolean isMarketWithCrop() {
        return (cropMajorCode != null) && (cropMidName != null) && (cropMinorName != null);
    }
}