package kr.co.webee.domain.gate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.gate.type.GateTimeActionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GateTimeCard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gate_id", nullable = false)
    private Gate gate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GateTimeActionType actionType;

    // OPEN_AT, CLOSE_AT, WINDOW에 사용. ALTERNATE_24H는 null
    private Integer startHour;

    // WINDOW에만 사용. 나머지는 null
    private Integer endHour;

    @Column(nullable = false)
    private boolean repeatEnabled;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Builder
    private GateTimeCard(Gate gate, GateTimeActionType actionType, Integer startHour, Integer endHour,
                         boolean repeatEnabled, String memo) {
        this.gate = Objects.requireNonNull(gate, "gate는 null이 될 수 없습니다.");
        this.actionType = Objects.requireNonNull(actionType, "actionType은 null이 될 수 없습니다.");
        this.startHour = startHour;
        this.endHour = endHour;
        this.repeatEnabled = repeatEnabled;
        this.memo = memo;
    }

    public void update(GateTimeActionType actionType, Integer startHour, Integer endHour,
                       boolean repeatEnabled, String memo) {
        this.actionType = Objects.requireNonNull(actionType, "actionType은 null이 될 수 없습니다.");
        this.startHour = startHour;
        this.endHour = endHour;
        this.repeatEnabled = repeatEnabled;
        this.memo = memo;
    }
}
