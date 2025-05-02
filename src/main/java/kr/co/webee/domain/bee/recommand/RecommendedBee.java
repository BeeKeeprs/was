package kr.co.webee.domain.bee.recommand;

import jakarta.persistence.*;
import kr.co.webee.domain.bee.BeeType;
import kr.co.webee.domain.profile.crop.UserCrop;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecommendedBee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BeeType beeType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_crop_id", nullable = false)
    private UserCrop userCrop;

    @Builder
    public RecommendedBee(BeeType beeType, String description, UserCrop userCrop) {
        if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("description은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.beeType = Objects.requireNonNull(beeType, "beeType은 null이 될 수 없습니다.");
        this.description = description;
        this.userCrop = Objects.requireNonNull(userCrop, "userCrop은 null이 될 수 없습니다.");
    }
}
