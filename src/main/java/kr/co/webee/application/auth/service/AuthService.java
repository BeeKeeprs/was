package kr.co.webee.application.auth.service;

import jakarta.persistence.EntityNotFoundException;
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
import kr.co.webee.presentation.auth.dto.request.PreOrderPhoneRequest;
import kr.co.webee.presentation.auth.dto.request.SignInRequest;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import kr.co.webee.presentation.auth.dto.response.PreOrderCheckResponse;
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
    private final static String PRE_ORDER_PHONE_NUMBER_REDIS_KEY = "pre-order:phones";

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

    public void savePreOrderPhoneNumber(PreOrderPhoneRequest request) {
        Long result = redisService.set(PRE_ORDER_PHONE_NUMBER_REDIS_KEY, request.phoneNumber());
        if (result == 0) {
            throw new BusinessException(ErrorType.ALREADY_EXIST_PHONE_NUMBER);
        }
    }

    public PreOrderCheckResponse checkPreOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String phoneNumber = user.getPhoneNumber();

        boolean isPreOrdered = redisService.isMemberOfSet(PRE_ORDER_PHONE_NUMBER_REDIS_KEY, phoneNumber);
        return PreOrderCheckResponse.of(isPreOrdered);
    }
}
