package kr.co.webee.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtConstants {
    public static final String ACCESS_TOKEN_HEADER_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_REDIS_KEY_PREFIX = "auth:refresh:";
    public static final String REFRESH_TOKEN_COOKIE_KEY = "refreshToken";
}
