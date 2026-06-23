package kr.co.webee.domain.hive.entity;

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
import kr.co.webee.domain.hive.type.GateActionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HiveGateAction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GateActionType actionType;

    @Column(nullable = false)
    private LocalTime actionTime;

    @Column(nullable = false)
    private boolean repeatEnabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Hive hive;

    @Builder
    private HiveGateAction(Hive hive, String title, GateActionType actionType, LocalTime actionTime, boolean repeatEnabled) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.title = title;
        this.actionType = Objects.requireNonNull(actionType, "actionType은 null이 될 수 없습니다.");
        this.actionTime = Objects.requireNonNull(actionTime, "actionTime은 null이 될 수 없습니다.");
        this.repeatEnabled = repeatEnabled;
        this.hive = Objects.requireNonNull(hive, "hive는 null이 될 수 없습니다.");
    }

    public void update(String title, GateActionType actionType, LocalTime actionTime, boolean repeatEnabled) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.title = title;
        this.actionType = Objects.requireNonNull(actionType, "actionType은 null이 될 수 없습니다.");
        this.actionTime = Objects.requireNonNull(actionTime, "actionTime은 null이 될 수 없습니다.");
        this.repeatEnabled = repeatEnabled;
    }
}
