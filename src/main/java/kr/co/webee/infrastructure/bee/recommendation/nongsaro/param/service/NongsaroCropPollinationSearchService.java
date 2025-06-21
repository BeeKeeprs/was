package kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.service;

import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.dto.NongsaroCropPollinationParamDto;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.io.NongsaroFileReader;
import kr.co.webee.infrastructure.bee.recommendation.nongsaro.param.model.NongsaroCropPollinationParamList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NongsaroCropPollinationSearchService {
    private final NongsaroFileReader nongsaroFileReader;

    public Optional<NongsaroCropPollinationParamDto> searchRequestParamBy(String crop, String variety) {
        NongsaroCropPollinationParamList cropPollinationParamList = nongsaroFileReader.getNongsaroCropPollinationParamList();
        return cropPollinationParamList.findRequestParamBy(crop, variety);
    }
}
