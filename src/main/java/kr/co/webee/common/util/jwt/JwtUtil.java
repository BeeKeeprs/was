package kr.co.webee.common.util.jwt;

import org.springframework.util.StringUtils;

public class JwtUtil {
    public static String parseToken(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(JwtConstants.ACCESS_TOKEN_HEADER_PREFIX)) {
            return authHeader.substring(JwtConstants.ACCESS_TOKEN_HEADER_PREFIX.length());
        }
        return "";
    }
}
