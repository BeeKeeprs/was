package kr.co.webee.infrastructure.bee.diagnosis.client;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.infrastructure.bee.diagnosis.dto.BeeDiseaseDetectResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class BeeDiseaseDetectClient {
    private final RestClient restClient;

    public BeeDiseaseDetectClient(RestClient.Builder restClientBuilder,
                                  @Value("${bee.diagnosis.api-url}") String apiUrl) {
        this.restClient = restClientBuilder
                .baseUrl(apiUrl)
                .build();
    }

    public BeeDiseaseDetectResultResponse.Prediction detect(MultipartFile image) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("image", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            });

            BeeDiseaseDetectResultResponse response = restClient.post()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(BeeDiseaseDetectResultResponse.class);

            log.info("질병 클래스 분류: {}, 신뢰도: {}", response.prediction().type(), response.prediction().confidence());

            return response.prediction();

        } catch (Exception e) {
            log.error("꿀벌 질병 탐지 실패: {}", e.getMessage());
            throw new BusinessException(ErrorType.BEE_DISEASE_DETECT_FAILED, "Failed to detect bee disease.");
        }
    }
}
