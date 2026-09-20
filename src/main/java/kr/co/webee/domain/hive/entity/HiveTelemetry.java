package kr.co.webee.domain.hive.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HiveTelemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Hive hive;

    private Double internalTemperature;

    private Double externalTemperature;

    private Double internalHumidity;

    private Double externalHumidity;

    private Double co2;

    private String peltierMode;

    private Double peltierDutyPct;

    private Double fanHotDutyPct;

    private Double fanColdDutyPct;

    private String fanState;

    private Double targetTemperature;

    private Boolean internalSensorValid;

    private Boolean externalSensorValid;

    private Double peltierCoolCurrentA;

    private Double peltierHeatCurrentA;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @Builder
    private HiveTelemetry(Hive hive, Double internalTemperature, Double externalTemperature,
                          Double internalHumidity, Double externalHumidity,
                          Double co2, String peltierMode, Double peltierDutyPct,
                          Double fanHotDutyPct, Double fanColdDutyPct,
                          String fanState, Double targetTemperature,
                          Boolean internalSensorValid, Boolean externalSensorValid,
                          Double peltierCoolCurrentA, Double peltierHeatCurrentA,
                          LocalDateTime recordedAt) {
        this.hive = Objects.requireNonNull(hive, "hive는 null이 될 수 없습니다.");
        this.internalTemperature = internalTemperature;
        this.externalTemperature = externalTemperature;
        this.internalHumidity = internalHumidity;
        this.externalHumidity = externalHumidity;
        this.co2 = co2;
        this.peltierMode = peltierMode;
        this.peltierDutyPct = peltierDutyPct;
        this.fanHotDutyPct = fanHotDutyPct;
        this.fanColdDutyPct = fanColdDutyPct;
        this.fanState = fanState;
        this.targetTemperature = targetTemperature;
        this.internalSensorValid = internalSensorValid;
        this.externalSensorValid = externalSensorValid;
        this.peltierCoolCurrentA = peltierCoolCurrentA;
        this.peltierHeatCurrentA = peltierHeatCurrentA;
        this.recordedAt = Objects.requireNonNull(recordedAt, "recordedAt은 null이 될 수 없습니다.");
    }
}
