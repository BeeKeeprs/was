package kr.co.webee.domain.oauth.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.oauth.enums.OAuthPlatform;
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OAuthPlatform platform;

    private String platformId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private OAuth(OAuthPlatform platform, String platformId, User user) {
        if (!StringUtils.hasText(platformId)) {
            throw new IllegalArgumentException("platformId는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.platform = Objects.requireNonNull(platform, "platform은 null이 될 수 없습니다.");
        this.platformId = platformId;
        this.user = Objects.requireNonNull(user, "user은 null이 될 수 없습니다.");
    }
}
