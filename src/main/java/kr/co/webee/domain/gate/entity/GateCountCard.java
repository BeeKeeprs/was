package kr.co.webee.domain.gate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.co.webee.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 벌 마릿수 기준 개폐기 제어 카드
 * <p>
 * repeatDays: 요일 비트마스크 (bit 0=일, 1=월, 2=화, 3=수, 4=목, 5=금, 6=토)
 * startHour/endHour: null이면 하루 종일 (0~24)
 * within*: minCount 이상 maxCount 미만 구간의 입/출구 개폐 여부
 * above*: maxCount 이상 구간의 입/출구 개폐 여부
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GateCountCard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int repeatDays;

    @Column(nullable = false)
    private int minCount;

    @Column(nullable = false)
    private int maxCount;

    // null이면 하루 종일
    private Integer startHour;
    private Integer endHour;

    @Column(nullable = false)
    private boolean withinEntranceOpen;

    @Column(nullable = false)
    private boolean withinExitOpen;

    @Column(nullable = false)
    private boolean aboveEntranceOpen;

    @Column(nullable = false)
    private boolean aboveExitOpen;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Builder
    private GateCountCard(Long userId, int repeatDays, int minCount, int maxCount,
                          Integer startHour, Integer endHour,
                          boolean withinEntranceOpen, boolean withinExitOpen,
                          boolean aboveEntranceOpen, boolean aboveExitOpen,
                          String memo) {
        this.userId = Objects.requireNonNull(userId, "userId는 null이 될 수 없습니다.");
        this.repeatDays = repeatDays;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.startHour = startHour;
        this.endHour = endHour;
        this.withinEntranceOpen = withinEntranceOpen;
        this.withinExitOpen = withinExitOpen;
        this.aboveEntranceOpen = aboveEntranceOpen;
        this.aboveExitOpen = aboveExitOpen;
        this.memo = memo;
    }

    public void update(int repeatDays, int minCount, int maxCount,
                       Integer startHour, Integer endHour,
                       boolean withinEntranceOpen, boolean withinExitOpen,
                       boolean aboveEntranceOpen, boolean aboveExitOpen,
                       String memo) {
        this.repeatDays = repeatDays;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.startHour = startHour;
        this.endHour = endHour;
        this.withinEntranceOpen = withinEntranceOpen;
        this.withinExitOpen = withinExitOpen;
        this.aboveEntranceOpen = aboveEntranceOpen;
        this.aboveExitOpen = aboveExitOpen;
        this.memo = memo;
    }
}
