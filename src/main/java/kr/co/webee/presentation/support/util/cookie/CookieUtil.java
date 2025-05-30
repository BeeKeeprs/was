package kr.co.webee.presentation.support.util.cookie;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie createCookie(String cookieName, String value, long maxAge) {
        return ResponseCookie.from(cookieName, value)
                .path("/")
                .httpOnly(true)
                .maxAge(maxAge)
                .secure(true)
                .sameSite("None")
                .build();
    }

    public static void deleteCookie(String cookieName, HttpServletResponse response) {
        ResponseCookie cookie = createCookie(cookieName, "", 0);
        response.addHeader("Set-Cookie", cookie.toString());
    }
}

