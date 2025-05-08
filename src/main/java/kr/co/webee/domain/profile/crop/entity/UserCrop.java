package kr.co.webee.domain.profile.crop.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.profile.crop.type.CultivationType;
import kr.co.webee.domain.user.entity.User;
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
public class UserCrop extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String variety;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CultivationType cultivationType;

    @Column(nullable = false)
    private String cultivationRegion;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Integer cultivationArea;

    @Column(nullable = false)
    private LocalDate plantingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserCrop(String name, String variety, CultivationType cultivationType,
                    String cultivationRegion, Double latitude, Double longitude,
                    Integer cultivationArea, LocalDate plantingDate, User user) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(cultivationRegion)) {
            throw new IllegalArgumentException("cultivationRegion은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.name = name;
        this.variety = variety;
        this.cultivationType = Objects.requireNonNull(cultivationType, "cultivationType은 null이 될 수 없습니다.");
        this.cultivationRegion = cultivationRegion;
        this.latitude = Objects.requireNonNull(latitude, "latitude는 null이 될 수 없습니다.");
        this.longitude = Objects.requireNonNull(longitude, "longitude는 null이 될 수 없습니다.");
        this.cultivationArea = Objects.requireNonNull(cultivationArea, "cultivationArea는 null이 될 수 없습니다.");
        this.plantingDate = Objects.requireNonNull(plantingDate, "plantingDate는 null이 될 수 없습니다.");
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
    }
}
