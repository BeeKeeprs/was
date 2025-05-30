package kr.co.webee.application.auth.helper;

import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.service.RefreshTokenService;
import kr.co.webee.common.auth.jwt.JwtProvider;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
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

        return JwtTokenDto.of(accessToken, refreshToken);
    }

    public JwtTokenDto reissueToken(String refreshToken) {
        Long userId = jwtProvider.getUserId(refreshToken);

        if (!refreshTokenService.existsByUserId(userId)) {
            throw new BusinessException(ErrorType.INVALID_REFRESH_TOKEN);
        }
        refreshTokenService.delete(userId);

        String username = jwtProvider.getUsername(refreshToken);
        String newAccessToken = jwtProvider.createAccessToken(username, userId);
        String newRefreshToken = jwtProvider.createRefreshToken(username, userId);

        refreshTokenService.save(userId, newRefreshToken);

        return JwtTokenDto.of(newAccessToken, newRefreshToken);
    }

    public void deleteRefreshToken(String refreshToken) {
        Long userId = jwtProvider.getUserId(refreshToken);
        refreshTokenService.delete(userId);
    }
}
