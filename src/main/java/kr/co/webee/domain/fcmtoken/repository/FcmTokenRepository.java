package kr.co.webee.domain.fcmtoken.repository;

import kr.co.webee.domain.fcmtoken.entity.FcmToken;
import kr.co.webee.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByUserIdAndDeviceInfo(Long userId, String deviceInfo);

    void deleteByUserIdAndDeviceInfo(Long userId, String deviceInfo);

    void deleteAllByUserId(Long userId);
}
