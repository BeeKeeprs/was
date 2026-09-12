package kr.co.webee.domain.gate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.gate.type.GateCardType;
import kr.co.webee.domain.gate.type.GateCommandStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GateCommand extends BaseTimeEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gate_id", nullable = false)
    private Gate gate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GateCardType cardType;

    @Column(columnDefinition = "TEXT")
    private String payloadJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GateCommandStatus status;

    private String detail;

    @Builder
    private GateCommand(String id, Gate gate, GateCardType cardType, String payloadJson) {
        this.id = Objects.requireNonNull(id);
        this.gate = Objects.requireNonNull(gate);
        this.cardType = Objects.requireNonNull(cardType);
        this.payloadJson = payloadJson;
        this.status = GateCommandStatus.PENDING;
    }

    public void complete(String resultStatus, String detail) {
        this.status = "OK".equals(resultStatus) ? GateCommandStatus.SUCCESS : GateCommandStatus.FAILED;
        this.detail = detail;
    }

    public void timeout() {
        this.status = GateCommandStatus.TIMEOUT;
    }
}
