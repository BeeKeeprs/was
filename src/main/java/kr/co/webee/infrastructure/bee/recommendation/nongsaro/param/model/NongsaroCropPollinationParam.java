package kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NongsaroCropPollinationParam {
    private final String crop;
    private final String variety;
    private final String menuId;
    private final String cntntsNo;

    public static NongsaroCropPollinationParam of(String crop, String variety, String menuId, String cntntsNo) {
        return new NongsaroCropPollinationParam(crop, variety, menuId, cntntsNo);
    }

    public boolean isSameCrop(String crop) {
        return this.crop.equals(crop);
    }

    public boolean isSameVariety(String variety) {
        return this.variety.equals(variety);
    }

    public boolean isSameCultivar(String crop, String variety) {
        return isSameCrop(crop) && (isSameVariety(variety) || "시중유통품종".equals(this.variety));
    }
}

