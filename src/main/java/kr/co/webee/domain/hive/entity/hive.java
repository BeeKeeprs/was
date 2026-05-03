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
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class hive extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private hive(String name, String region, String location, String serialNumber, String memo, User user) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(region)) {
            throw new IllegalArgumentException("region은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(location)) {
            throw new IllegalArgumentException("location은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(serialNumber)) {
            throw new IllegalArgumentException("serialNumber는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.name = name;
        this.region = region;
        this.location = location;
        this.serialNumber = serialNumber;
        this.memo = memo;
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
    }
}
