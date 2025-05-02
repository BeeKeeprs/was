package kr.co.webee.domain.bee;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BeeType{
    HONEYBEE("꿀벌"),
    ASIAN_BUMBLEBEE("호박벌"),
    EUROPEAN_BUMBLEBEE("서양뒤영벌"),
    MASON_BEE("머리뿔가위벌");

    private final String description;
}
