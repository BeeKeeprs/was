package kr.co.webee.domain.bee.recommend.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.profile.crop.entity.UserCrop;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BeeRecommendation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BeeType beeType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String characteristics;

    @Column(nullable = false)
    private LocalDate inputStartDate;

    @Column(nullable = false)
    private LocalDate inputEndDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String caution;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String usageTip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_crop_id", nullable = false)
    private UserCrop userCrop;

    @Builder
    public BeeRecommendation(BeeType beeType, String characteristics, LocalDate inputStartDate,
                             LocalDate inputEndDate, String caution, String usageTip, UserCrop userCrop) {
        if (!StringUtils.hasText(characteristics)) {
            throw new IllegalArgumentException("characteristics은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(caution)) {
            throw new IllegalArgumentException("caution은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(usageTip)) {
            throw new IllegalArgumentException("usageTip은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.beeType = Objects.requireNonNull(beeType, "beeType은 null이 될 수 없습니다.");
        this.characteristics = characteristics;
        this.inputStartDate=Objects.requireNonNull(inputStartDate, "inputStartDate는 null이 될 수 없습니다.");
        this.inputEndDate=Objects.requireNonNull(inputEndDate, "inputEndDate는 null이 될 수 없습니다.");
        this.caution=caution;
        this.usageTip=usageTip;
        this.userCrop = Objects.requireNonNull(userCrop, "userCrop은 null이 될 수 없습니다.");
    }
}
