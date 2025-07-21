package kr.co.webee.infrastructure.businesscert.client;

import kr.co.webee.infrastructure.businesscert.dto.BusinessCertificateInfoRequest;
import kr.co.webee.infrastructure.businesscert.dto.BusinessCertificateValidationRequest;
import kr.co.webee.infrastructure.businesscert.dto.BusinessCertificateValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessCertificateValidationClient {
    private final String PUBLIC_BUSINESS_CERTIFICATE_VALIDATION_API_URL = "https://api.odcloud.kr/api/nts-businessman/v1/validate";
    private final String AUTHORIZATION_REQUEST_PARAMETER_KEY = "serviceKey";

    @Value("${business-certificate-validation.api-key}")
    private String apiKey;

    private final RestClient restClient;

    public BusinessCertificateValidationClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(PUBLIC_BUSINESS_CERTIFICATE_VALIDATION_API_URL)
                .build();
    }

    public boolean validateBusinessCertificate(BusinessCertificateInfoRequest businessCertificateInfo) {
        List<BusinessCertificateInfoRequest> requests = new ArrayList<>();
        requests.add(businessCertificateInfo);
        BusinessCertificateValidationRequest validationRequest = BusinessCertificateValidationRequest.of(requests);

        BusinessCertificateValidationResponse response = restClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam(AUTHORIZATION_REQUEST_PARAMETER_KEY,
                        URLDecoder.decode(apiKey, StandardCharsets.UTF_8)).build())
                .body(validationRequest)
                .retrieve()
                .body(BusinessCertificateValidationResponse.class);

        if (response == null || response.data().isEmpty()) {
            throw new IllegalArgumentException("사업자등록인증 API 호출에 실패했습니다.");
        }

        String validCode = response.data().get(0).valid();
        return "01".equals(validCode);
    }
}
