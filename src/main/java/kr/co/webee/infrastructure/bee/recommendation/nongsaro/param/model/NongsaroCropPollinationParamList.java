package kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.model;

import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.dto.NongsaroCropPollinationParamDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NongsaroCropPollinationParamList {
    private final List<NongsaroCropPollinationParam> list;

    public static NongsaroCropPollinationParamList of(List<NongsaroCropPollinationParam> list) {
        return new NongsaroCropPollinationParamList(list);
    }

    public Optional<NongsaroCropPollinationParamDto> findRequestParamBy(String crop, String variety) {
        return list.stream()
                .filter(entry -> entry.isSameCultivar(crop, variety))
                .findFirst()
                .map(NongsaroCropPollinationParamDto::from);
    }
}