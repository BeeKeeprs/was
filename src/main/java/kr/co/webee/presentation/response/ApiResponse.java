package kr.co.webee.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNullElse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        String code,
        T data,
        String message
) {
    public static <T> ApiResponse<?> success(T data) {
        return new ApiResponse<>(
                "OK",
                requireNonNullElse(data, emptyMap()),
                "요청이 성공적으로 처리되었습니다."
        );
    }

    public static ApiResponse<?> fail(ApiErrorDto error) {
        return new ApiResponse<>(
                error.getCode(),
                null,
                error.getMessage()
        );
    }
}
