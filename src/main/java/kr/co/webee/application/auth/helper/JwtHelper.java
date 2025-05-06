package kr.co.webee.application.auth.helper;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.service.RefreshTokenService;
import kr.co.webee.common.auth.JwtProvider;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.common.util.cookie.CookieUtil;
import kr.co.webee.common.util.jwt.JwtConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtHelper {
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public JwtTokenDto createToken(Long userId, String username) {
        String accessToken = jwtProvider.createAccessToken(username, userId);
        String refreshToken = jwtProvider.createRefreshToken(username, userId);

        refreshTokenService.save(userId, refreshToken);

        return JwtTokenDto.of(accessToken,refreshToken);
    }

    public JwtTokenDto reissueToken(String refreshToken) {
        Long userId = jwtProvider.getUserId(refreshToken);

        if (!refreshTokenService.existsByUserId(userId)) {
            throw new BusinessException(ErrorType.INVALID_ACCESS_TOKEN);
        }
        refreshTokenService.delete(userId);

        String username = jwtProvider.getUsername(refreshToken);
        String newAccessToken = jwtProvider.createAccessToken(username, userId);
        String newRefreshToken = jwtProvider.createRefreshToken(username, userId);

        refreshTokenService.save(userId, newRefreshToken);

        return JwtTokenDto.of(newAccessToken, newRefreshToken);
    }

    public void deleteRefreshToken(String refreshToken, HttpServletResponse response) {
        Long userId = jwtProvider.getUserId(refreshToken);
        CookieUtil.deleteCookie(JwtConstants.REFRESH_TOKEN_COOKIE_KEY, response);
        refreshTokenService.delete(userId);
    }
}
