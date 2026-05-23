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

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @Builder
    private HiveTelemetry(Hive hive, Double internalTemperature, Double externalTemperature,
                          Double internalHumidity, Double externalHumidity,
                          Double co2, LocalDateTime recordedAt) {
        this.hive = Objects.requireNonNull(hive, "hive는 null이 될 수 없습니다.");
        this.internalTemperature = internalTemperature;
        this.externalTemperature = externalTemperature;
        this.internalHumidity = internalHumidity;
        this.externalHumidity = externalHumidity;
        this.co2 = co2;
        this.recordedAt = Objects.requireNonNull(recordedAt, "recordedAt은 null이 될 수 없습니다.");
    }
}
