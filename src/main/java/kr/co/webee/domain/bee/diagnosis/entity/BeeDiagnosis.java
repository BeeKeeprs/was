package kr.co.webee.domain.bee.diagnosis.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.bee.diagnosis.type.DiseaseType;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
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

    @ColumnDefault("false")
    private boolean isDiseased;

    @Enumerated(EnumType.STRING)
    private DiseaseType diseaseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BeeDiagnosis(String imageUrl, boolean isDiseased, DiseaseType diseaseType, User user) {
        if (!StringUtils.hasText(imageUrl)) {
            throw new IllegalArgumentException("imageUrl는 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.imageUrl = imageUrl;
        this.isDiseased = isDiseased;
        this.diseaseType=diseaseType;
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
    }
}
