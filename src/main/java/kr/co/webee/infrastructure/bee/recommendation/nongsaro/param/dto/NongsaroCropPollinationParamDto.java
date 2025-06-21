package kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.dto;

import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.model.NongsaroCropPollinationParam;

public record NongsaroCropPollinationParamDto(
        String menuId,
        String cntntsNo
) {
    public static NongsaroCropPollinationParamDto from(NongsaroCropPollinationParam requestInfo) {
        return new NongsaroCropPollinationParamDto(requestInfo.getMenuId(), requestInfo.getCntntsNo());
    }
}
