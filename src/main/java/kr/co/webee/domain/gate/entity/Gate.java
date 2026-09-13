package kr.co.webee.domain.gate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Gate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String macAddress;

    @Column(nullable = false)
    private String name;

    private String region;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(nullable = false)
    private boolean isConnected;

    private LocalDateTime lastConnectedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateConnection(boolean isConnected) {
        this.isConnected = isConnected;
        if (isConnected) {
            this.lastConnectedAt = LocalDateTime.now();
        }
    }

    @Builder
    private Gate(String macAddress, String name, String region, String location, String memo, User user) {
        if (!StringUtils.hasText(macAddress)) {
            throw new IllegalArgumentException("macAddress는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.macAddress = macAddress;
        this.name = name;
        this.region = region;
        this.location = location;
        this.memo = memo;
        this.isConnected = false;
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
    }
}