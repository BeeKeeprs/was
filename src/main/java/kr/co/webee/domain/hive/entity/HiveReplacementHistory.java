package kr.co.webee.domain.hive.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HiveReplacementHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Hive hive;

    @Column(nullable = false)
    private LocalDate replacedAt;

    private Long usageDays;

    @Builder
    private HiveReplacementHistory(Hive hive, LocalDate replacedAt) {
        this.hive = Objects.requireNonNull(hive, "hive는 null이 될 수 없습니다.");
        this.replacedAt = Objects.requireNonNull(replacedAt, "replacedAt은 null이 될 수 없습니다.");
    }

    public void updateUsageDays(long usageDays) {
        if (usageDays < 0) {
            throw new IllegalArgumentException("usageDays는 0 이상이어야 합니다.");
        }
        this.usageDays = usageDays;
    }

    public void clearUsageDays() {
        this.usageDays = null;
    }

    public void updateReplacedAt(LocalDate replacedAt) {
        this.replacedAt = Objects.requireNonNull(replacedAt, "replacedAt은 null이 될 수 없습니다.");
    }
}
