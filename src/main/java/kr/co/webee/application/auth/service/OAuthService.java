package kr.co.webee.application.auth.service;

import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.dto.OAuthSignInDto;
import kr.co.webee.application.auth.helper.JwtHelper;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.oauth.entity.OAuth;
import kr.co.webee.domain.oauth.enums.OAuthPlatform;
import kr.co.webee.domain.oauth.repository.OAuthRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.oauth.client.OAuthClient;
import kr.co.webee.infrastructure.oauth.dto.OAuthUserInfoDto;
import kr.co.webee.presentation.auth.dto.request.UserInfoRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class OAuthService {
    private final UserRepository userRepository;
    private final OAuthRepository oAuthRepository;
    private final JwtHelper jwtHelper;
    private final OAuthClientFactory oAuthClientFactory;

    @Transactional
    public OAuthSignInDto signIn(OAuthPlatform platform, String code) {
        OAuthClient oAuthClient = oAuthClientFactory.getOAuthClient(platform);

        String accessToken = oAuthClient.getAccessToken(code);
        OAuthUserInfoDto userInfo = oAuthClient.getUserInfo(accessToken);

        return issueToken(platform, userInfo);
    }

    private OAuthSignInDto issueToken(OAuthPlatform platform, OAuthUserInfoDto userInfo) {
        String username = platform + "_" + userInfo.platformId();

        return userRepository.findByUsername(username)
                .map(user -> {
                    return buildSignInResponse(user, false);
                })
                .orElseGet(() -> {
                    User registeredUser = registerUser(platform, userInfo);
                    return buildSignInResponse(registeredUser, true);
                });
    }

    @Transactional
    public void registerUserInfo(UserInfoRegisterRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.ENTITY_NOT_FOUND));

        user.updatePhoneNumber(request.phoneNumber());
    }

    private OAuthSignInDto buildSignInResponse(User user, boolean isNewUser) {
        JwtTokenDto token = jwtHelper.createToken(user.getId(), user.getUsername());
        return OAuthSignInDto.of(isNewUser, user.getName(), token);
    }

    private User registerUser(OAuthPlatform platform, OAuthUserInfoDto userInfo) {
        String username = platform + "_" + userInfo.platformId();

        User user = User.builder()
                .username(username)
                .name(username)
                .build();

        OAuth oAuth = OAuth.builder()
                .platform(platform)
                .platformId(userInfo.platformId())
                .user(user)
                .build();

        oAuthRepository.save(oAuth);

        return user;
    }
}