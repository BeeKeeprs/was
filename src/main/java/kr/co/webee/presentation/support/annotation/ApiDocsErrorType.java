package kr.co.webee.presentation.support.annotation;

import kr.co.webee.common.error.ErrorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 컨트롤러 메서드에 대한 API 문서화 시 에러 타입을 명시하는 어노테이션입니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDocsErrorType {
    ErrorType[] value();
}
