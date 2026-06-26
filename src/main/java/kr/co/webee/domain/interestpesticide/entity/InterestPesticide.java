package kr.co.webee.domain.interestpesticide.entity;

import jakarta.persistence.Column;
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
public class InterestPesticide extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pesticideApplicationNo;

    @Column
    private String brandName;

    @Column
    private String productName;

    @Column
    private String contentInfo;

    @Column
    private String safeSprayInterval;

    @Column
    private String cropName;

    @Column
    private String insectName;

    @Column
    private String usageName;

    @Column
    private String targetPestName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private InterestPesticide(String pesticideApplicationNo, String brandName, String productName,
                              String contentInfo, String safeSprayInterval, String cropName,
                              String insectName, String usageName, String targetPestName, User user) {
        if (!StringUtils.hasText(pesticideApplicationNo)) {
            throw new IllegalArgumentException("pesticideApplicationNo는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.pesticideApplicationNo = pesticideApplicationNo;
        this.brandName = brandName;
        this.productName = productName;
        this.contentInfo = contentInfo;
        this.safeSprayInterval = safeSprayInterval;
        this.cropName = cropName;
        this.insectName = insectName;
        this.usageName = usageName;
        this.targetPestName = targetPestName;
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
    }
}
