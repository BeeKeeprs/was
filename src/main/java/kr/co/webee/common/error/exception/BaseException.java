package kr.co.webee.common.error.exception;

import kr.co.webee.common.error.ErrorType;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorType type;
    private final String message;

    public BaseException(ErrorType type, String message) {
        super(message);
        this.type = type;
        this.message = message;
    }

    @Override
    public String toString() {
        return "에러 타입: " + type + ", 사유: " + message;
    }
}
