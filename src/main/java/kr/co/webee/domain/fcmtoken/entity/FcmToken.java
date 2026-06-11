package kr.co.webee.domain.fcmtoken.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String deviceInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private FcmToken(String token, String deviceInfo, User user) {
        this.token = token;
        this.deviceInfo = deviceInfo;
        this.user = user;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}
