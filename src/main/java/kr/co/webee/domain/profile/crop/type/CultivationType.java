package kr.co.webee.domain.profile.crop.type;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CultivationType {

    CONTROLLED("시설 재배"),
    OPEN_FIELD("노지");

    private final String description;
}
