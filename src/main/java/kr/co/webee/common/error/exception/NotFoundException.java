package kr.co.webee.common.error.exception;

import kr.co.webee.common.error.ErrorType;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(ErrorType.ENTITY_NOT_FOUND, message);
    }

    public <ID> NotFoundException(Class<?> clazz, ID id) {
        super(ErrorType.ENTITY_NOT_FOUND, clazz.getSimpleName() + ": id (" + id + ") is not found");
    }
}
