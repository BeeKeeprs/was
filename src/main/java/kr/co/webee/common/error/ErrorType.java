package kr.co.webee.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static kr.co.webee.common.error.ErrorType.Level.*;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum ErrorType {
    // WebErrorType
    UNHANDLED_EXCEPTION(INTERNAL_SERVER_ERROR, ERROR, "WEB_001", "직접적으로 처리되지 않은 예외, 문의해주세요"),
    FAILED_VALIDATION(BAD_REQUEST, WARN, "WEB_002", "Request 요청에서 올바르지 않은 값이 있습니다"),
    FAILED_PARSING(BAD_REQUEST, WARN, "WEB_003", "Request JSON body를 파싱하지 못했습니다"),
    UNSUPPORTED_API(BAD_REQUEST, DEBUG, "WEB_004", "지원하지 않는 API입니다"),
    COOKIE_NOT_FOND(BAD_REQUEST, DEBUG, "WEB_005", "요청에 쿠키가 필요합니다"),

    // AuthErrorType
    FAILED_AUTHENTICATION(UNAUTHORIZED, INFO, "AUTH_001", "인증에 실패하였습니다"),
    INVALID_ACCESS_TOKEN(UNAUTHORIZED, DEBUG, "AUTH_002", "유효하지 않은 토큰입니다"),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, DEBUG, "AUTH_003", "만료된 토큰입니다"),
    MALFORMED_ACCESS_TOKEN(UNAUTHORIZED, DEBUG, "AUTH_004", "잘못된 형식의 토큰입니다"),
    TAMPERED_ACCESS_TOKEN(UNAUTHORIZED, DEBUG, "AUTH_005", "변조된 토큰입니다"),
    UNSUPPORTED_JWT_TOKEN(UNAUTHORIZED, DEBUG, "AUTH_006", "지원하지 않는 JWT 토큰입니다"),
    UNSUPPORTED_SOCIAL_TYPE(UNAUTHORIZED, DEBUG, "AUTH_007", "지원하지 않는 소셜 타입입니다"),
    UNKNOWN_TOKEN_ERROR(UNAUTHORIZED, DEBUG,"AUTH_008","알 수 없는 JWT 토큰 에러입니다."),
    INVALID_CREDENTIALS(UNAUTHORIZED, DEBUG, "AUTH_009", "해당 사용자의 정보가 없거나 일치하지 않아 처리할 수 없습니다"),
    ACCESS_DENIED(FORBIDDEN, DEBUG, "COMMON_002", "접근 권한이 없습니다"),

    // OauthErrorType
    SOCIAL_MEMBER_NOT_FOUND(NOT_FOUND, DEBUG, "OAUTH_001", "찾을 수 없는 소셜 회원입니다"),
    EMAIL_NOT_FOUND_IN_ID_TOKEN(BAD_REQUEST, DEBUG, "OAUTH_002", "ID 토큰 필드에 이메일이 없습니다"),

    // DatabaseErrorType
    ENTITY_NOT_FOUND(NOT_FOUND, DEBUG, "DB_001", "해당 엔티티를 찾을 수 없습니다"),
    VIOLATION_OCCURRED(NOT_ACCEPTABLE, ERROR, "DB_002", "저장할 수 없는 값입니다"),

    // UserErrorType
    ALREADY_EXIST_USERNAME(CONFLICT, WARN, "USER_001", "이미 존재하는 아이디입니다"),

    // FileErrorType
    FILE_UPLOAD_FAILED(BAD_GATEWAY, ERROR, "FILE_001", "파일 업로드에 실패했습니다"),

    ;

    private final HttpStatus httpStatus;
    private final Level logLevel;
    private final String code;
    private final String message;

    public enum Level {
        INFO,
        DEBUG,
        WARN,
        ERROR
    }
}
