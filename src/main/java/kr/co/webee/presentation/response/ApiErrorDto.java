package kr.co.webee.presentation.response;

import jakarta.validation.constraints.NotNull;
import kr.co.webee.common.error.ErrorType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

// TODO : @Schema 어노테이션을 사용하여 API 문서화에 필요한 정보를 추가
//@Schema(description = "API 애러 응답 형식")
@Getter
@ToString
@Builder(access = PRIVATE)
public class ApiErrorDto {
//    @Schema(description = "응답 상태 코드", example = "400")
    private final int statusCode;
//    @Schema(description = "응답 코드 이름 (디버깅용)", example = "FAILED_AUTHENTICATION")
    private final String code;
//    @Schema(description = "응답 메시지 (디버깅용)", example = "인증에 실패했습니다")
    private final String message;

    public static ApiErrorDto of(
            @NotNull ErrorType type,
            String message
    ) {
        return ApiErrorDto.builder()
                .statusCode(type.getHttpStatus().value())
                .code(type.getCode())
                .message(type.getMessage() + ", " + message)
                .build();
    }
}
