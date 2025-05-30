package kr.co.webee.presentation.support.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BaseException;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.presentation.support.response.ApiErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ExceptionRestHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorDto> handleBaseException(HttpServletRequest request, BaseException e) {
        return toResponseEntity(request, e.getType(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorDto> handleBusinessException(HttpServletRequest request, BusinessException e) {
        return toResponseEntity(request, e.getType(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleException(HttpServletRequest request, Exception e) {
        log.error(
                "Unhandled exception[{}]: {}, method: {}, uri: {}",
                e.getClass(),
                e.getMessage(),
                request.getMethod(),
                request.getRequestURI());
        return toResponseEntity(request, ErrorType.UNHANDLED_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorDto> handleNoResourceFoundException(HttpServletRequest request, NoResourceFoundException e) {
        return toResponseEntity(request, ErrorType.UNSUPPORTED_API, e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorDto> handleHttpRequestMethodNotSupportedException(HttpServletRequest request,
                                                                                    HttpRequestMethodNotSupportedException e) {
        return toResponseEntity(request, ErrorType.UNSUPPORTED_API, e.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiErrorDto> handleInsufficientAuthenticationException(HttpServletRequest request,
                                                                                 InsufficientAuthenticationException e) {
        return toResponseEntity(request, ErrorType.FAILED_AUTHENTICATION, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDto> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        return toResponseEntity(request, ErrorType.FAILED_VALIDATION, e.getMessage());
    }

    /// /////////////// 요청 파라미터 예외 / 타입 불일치, Enum 매개변수 관련 예외
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorDto> handleMethodArgumentTypeMismatchException(HttpServletRequest request,
                                                                                 MethodArgumentTypeMismatchException ex) {

        String parameterName = ex.getName(); // 파라미터 이름
        Object receivedValue = ex.getValue(); // 잘못된 값
        Class<?> requiredType = ex.getRequiredType(); // 기대하는 타입
        String requiredTypeName =
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown type"; // 기대하는 타입 이름
        String actualTypeName = receivedValue != null ? receivedValue.getClass().getSimpleName() : "null"; // 받은 타입

        String expectedTypeError = "";
        if (!requiredTypeName.equals(actualTypeName)) {
            expectedTypeError = String.format(
                    "Expected type: %s, but received type: %s; ",
                    requiredType != null ? requiredType.getSimpleName() : "unknown type",
                    receivedValue != null ? receivedValue.getClass().getName() : "null");
        }
        // requiredType이 Enum타입일 경우
        String expectedValuesError = "";
        if (requiredType != null && requiredType.isEnum()) {
            String enumValues = Arrays.stream(requiredType.getEnumConstants())
                    .map(enumConstant -> ((Enum<?>) enumConstant).name())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("none");
            expectedValuesError = String.format(" Expected values: [%s]; ", enumValues);
        }
        String errorMessage = String.format(
                "Invalid value for parameter '%s' for value[%s]; %s%s",
                parameterName, receivedValue, expectedTypeError, expectedValuesError);

        log.warn("{}; {}", errorMessage, ex.getMessage());
        return toResponseEntity(request, ErrorType.FAILED_VALIDATION, errorMessage);
    }

    /// /////////////// 직렬화 / 역직렬화 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        return toResponseEntity(request,
                ErrorType.FAILED_VALIDATION,
                ex.getBindingResult().getAllErrors().toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDto> handleHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        return toResponseEntity(request, ErrorType.FAILED_PARSING, ex);
    }

    /// /////////////// Database 관련 예외
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDto> handleDataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException ex) {
        return toResponseEntity(request, ErrorType.VIOLATION_OCCURRED, ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDto> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {
        return toResponseEntity(request, ErrorType.VIOLATION_OCCURRED, ex);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleEntityNotFoundException(HttpServletRequest request, EntityNotFoundException ex) {
        return toResponseEntity(request, ErrorType.ENTITY_NOT_FOUND, ex);
    }

    private static ResponseEntity<ApiErrorDto> toResponseEntity(HttpServletRequest request, @NotNull ErrorType type, Exception exception) {
        return toResponseEntity(request, type, exception.getClass().getName() + ": " + exception.getMessage());
    }

    private static ResponseEntity<ApiErrorDto> toResponseEntity(HttpServletRequest request, @NotNull ErrorType type, String message) {
        String errorMessage = type.getMessage() + ": " + message + " URL: " + request.getRequestURI();
        loggingExceptionByErrorType(type, errorMessage);
        return ResponseEntity.status(type.getHttpStatus().value()).body(ApiErrorDto.of(type, message));
    }

    private static void loggingExceptionByErrorType(ErrorType type, String message) {
        switch (type.getLogLevel()) {
            case INFO -> log.info(message);
            case DEBUG -> log.debug(message);
            case WARN -> log.warn(message);
            default -> log.error(message);
        }
    }
}
