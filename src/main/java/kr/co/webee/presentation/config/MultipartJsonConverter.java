package kr.co.webee.presentation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

/**
 * MultipartJsonConverter는 JSON 데이터를 MultipartFile로 변환하는 컨버터입니다.
 * <p>
 * 이 컨버터는 JSON 데이터를 MultipartFile로 변환하여 서버에 전송할 수 있도록 도와줍니다.
 * Swagger 문서화에만 사용되며, 실제 서비스 로직이나 비즈니스 로직과는 무관합니다.
 * <p>
 * @see org.springframework.web.multipart.MultipartFile
 */
@Component
public class MultipartJsonConverter extends AbstractJackson2HttpMessageConverter {

    public MultipartJsonConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }
}
