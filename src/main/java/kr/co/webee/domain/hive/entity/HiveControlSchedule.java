package kr.co.webee.domain.hive.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.webee.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HiveControlSchedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Hive hive;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean enabled;

    @Builder
    private HiveControlSchedule(Hive hive, LocalTime startTime, LocalTime endTime, boolean enabled) {
        this.hive = Objects.requireNonNull(hive, "hive는 null이 될 수 없습니다.");
        this.startTime = Objects.requireNonNull(startTime, "startTime은 null이 될 수 없습니다.");
        this.endTime = Objects.requireNonNull(endTime, "endTime은 null이 될 수 없습니다.");
        this.enabled = Objects.requireNonNull(enabled, "enabled는 null이 될 수 없습니다.");
    }
}
