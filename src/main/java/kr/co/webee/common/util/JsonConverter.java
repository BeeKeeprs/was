package kr.co.webee.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonConverter {
    private final ObjectMapper objectMapper;

    /**
     * Object -> JSON String
     */
    public String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 직렬화에 실패했습니다.", e);
        }
    }

    /**
     * JSON String -> Object
     */
    public <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 역직렬화에 실패했습니다.", e);
        }
    }

    /**
     * Object (Map, JSON String 등) -> Target Type
     */
    public <T> T convert(Object value, Class<T> type) {
        if (value instanceof String str) {
            return fromJson(str, type);
        }
        return objectMapper.convertValue(value, type);
    }
}
