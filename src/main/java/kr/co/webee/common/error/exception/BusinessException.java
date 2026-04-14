package kr.co.webee.common.error.exception;

import kr.co.webee.common.error.ErrorType;

public class BusinessException extends BaseException {
    public BusinessException(ErrorType type, String message) {
        super(type, message);
    }

    public BusinessException(ErrorType type) {
        super(type, "No message");
    }
}
