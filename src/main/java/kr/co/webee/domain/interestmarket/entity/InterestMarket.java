package kr.co.webee.domain.interestmarket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.webee.domain.common.BaseTimeEntity;
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
public class InterestMarket extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marketCode;

    private String cropMajorCode;

    private String cropMidName;

    private String cropMinorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private InterestMarket(String marketCode, String cropMajorCode, String cropMidName, String cropMinorName, User user) {
        if (!StringUtils.hasText(marketCode)) {
            throw new IllegalArgumentException("marketCode는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.marketCode=marketCode;
        this.cropMajorCode = cropMajorCode;
        this.cropMidName = cropMidName;
        this.cropMinorName = cropMinorName;
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
    }
}

