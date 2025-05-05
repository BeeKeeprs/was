package kr.co.webee.application.auth.dto;

public record JwtTokenDto(
        String accessToken,
        String refreshToken
) {
    public static JwtTokenDto of(String accessToken, String refreshToken) {
        return new JwtTokenDto(accessToken, refreshToken);
    }
}