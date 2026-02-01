package kr.co.webee.application.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.dto.SignInDto;
import kr.co.webee.application.auth.helper.JwtHelper;
import kr.co.webee.common.constant.JwtConstants;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.redis.service.RedisService;
import kr.co.webee.presentation.auth.dto.request.PreorderPhoneRequest;
import kr.co.webee.presentation.auth.dto.request.SignInRequest;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import kr.co.webee.presentation.support.util.cookie.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final static String PREORDER_PHONE_NUMBER_REDIS_KEY = "preorder:phones";

    public void signup(SignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            String message = String.format("username: %s", request.username());
            throw new BusinessException(ErrorType.ALREADY_EXIST_USERNAME, message);
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new BusinessException(ErrorType.ALREADY_EXIST_PHONE_NUMBER);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = request.toEntity(encodedPassword);
        userRepository.save(user);
    }

    public SignInDto signIn(SignInRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> {
                    String message = String.format("username: %s", request.username());
                    return new BusinessException(ErrorType.INVALID_CREDENTIALS, message);
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorType.FAILED_AUTHENTICATION);
        }

        JwtTokenDto token = jwtHelper.createToken(user.getId(), user.getUsername());
        return SignInDto.of(user.getName(), token);
    }

    public JwtTokenDto reissueToken(String refreshToken) {
        return jwtHelper.reissueToken(refreshToken);
    }

    public void signOut(String refreshToken, HttpServletResponse response) {
        jwtHelper.deleteRefreshToken(refreshToken);
        CookieUtil.deleteCookie(JwtConstants.REFRESH_TOKEN_COOKIE_KEY, response);
    }

    public void savePreorderPhoneNumber(PreorderPhoneRequest request) {
        if (redisService.isMemberOfSet(PREORDER_PHONE_NUMBER_REDIS_KEY, request.phoneNumber())) {
            throw new BusinessException(ErrorType.ALREADY_EXIST_PHONE_NUMBER);
        }

        redisService.set(PREORDER_PHONE_NUMBER_REDIS_KEY,request.phoneNumber());
    }
}
