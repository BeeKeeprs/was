package kr.co.webee.application.fcmtoken.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.fcmtoken.repository.FcmTokenRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.fcmtoken.dto.request.FcmTokenRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registerToken(Long userId, FcmTokenRegisterRequest request) {
        fcmTokenRepository.findByUserIdAndDeviceInfo(userId, request.deviceInfo())
                .ifPresentOrElse(
                        // 기존에 fcm token이 존재할 경우 update
                        fcmToken -> fcmToken.updateToken(request.token()),

                        // 기존에 fcm token이 없을 경우 새로 생성
                        () -> {
                            User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

                            fcmTokenRepository.save(request.toEntity(user));
                        }
                );
    }

    @Transactional
    public void deleteToken(Long userId, String deviceInfo) {
        fcmTokenRepository.deleteByUserIdAndDeviceInfo(userId, deviceInfo);
    }
}
