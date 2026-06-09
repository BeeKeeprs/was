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
import kr.co.webee.domain.hive.type.ControlType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HiveControl extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Hive hive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ControlType type;

    @Column(nullable = false)
    private boolean autoEnabled;

    @Column(nullable = false)
    private boolean manualEnabled;

    @Column(nullable = false)
    private boolean isOn;

    @Builder
    private HiveControl(Hive hive, ControlType type, boolean autoEnabled, boolean manualEnabled, boolean isOn) {
        this.hive = Objects.requireNonNull(hive, "hive는 null이 될 수 없습니다.");
        this.type = Objects.requireNonNull(type, "type은 null이 될 수 없습니다.");
        this.autoEnabled = autoEnabled;
        this.manualEnabled = manualEnabled;
        this.isOn = isOn;
    }

    public static HiveControl createAutoControl(Hive hive, ControlType type, boolean autoEnabled) {
        return HiveControl.builder()
                .hive(hive)
                .type(type)
                .autoEnabled(autoEnabled)
                .manualEnabled(false)
                .isOn(false)
                .build();
    }

    public void updateAutoEnabled(boolean autoEnabled) {
        this.autoEnabled = autoEnabled;
    }
}