package kr.co.webee.infrastructure.sms.util;

import java.security.SecureRandom;

public class SmsUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateAuthCode() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public static String makeAuthMessage(String authCode) {
        return "[webee 인증번호] " + authCode + "\n본인 확인을 위해 인증번호를 입력해주세요.";
    }
}