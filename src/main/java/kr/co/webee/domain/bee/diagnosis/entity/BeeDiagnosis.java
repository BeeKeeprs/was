package kr.co.webee.domain.bee.diagnosis.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.bee.diagnosis.type.DiseaseType;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.profile.crop.type.CultivationType;
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BeeDiagnosis extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private DiseaseType diseaseType;

    @Column(nullable = false)
    private Double confidence;

    @Column(nullable = false)
    private String cropName;

    @Column(nullable = false)
    private CultivationType cultivationType;

    @Column(nullable = false)
    private String cultivationAddress;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private String situationAnalysis;

    @Column(nullable = false)
    private String solutions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public BeeDiagnosis(String imageUrl, DiseaseType diseaseType, Double confidence,
                        String cropName, CultivationType cultivationType, String cultivationAddress,
                        String details, String situationAnalysis, String solutions, User user) {
        if (!StringUtils.hasText(imageUrl)) {
            throw new IllegalArgumentException("imageUrl는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(cropName)) {
            throw new IllegalArgumentException("cropName는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(cultivationAddress)) {
            throw new IllegalArgumentException("cultivationAddress는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(details)) {
            throw new IllegalArgumentException("details는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(situationAnalysis)) {
            throw new IllegalArgumentException("situationAnalysis는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(solutions)) {
            throw new IllegalArgumentException("solutions는 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.imageUrl = imageUrl;
        this.diseaseType=Objects.requireNonNull(diseaseType, "diseaseType는 null이 될 수 없습니다.");
        this.confidence=Objects.requireNonNull(confidence, "confidence는 null이 될 수 없습니다.");
        this.cropName=cropName;
        this.cultivationType = Objects.requireNonNull(cultivationType, "cultivationType는 null이 될 수 없습니다.");
        this.cultivationAddress=cultivationAddress;
        this.details=details;
        this.situationAnalysis=situationAnalysis;
        this.solutions=solutions;
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
    }
}
