package kr.co.webee.domain.user.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import java.util.Objects;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @ColumnDefault("false")
    private boolean businessRegistered;

    @Builder
    public User(String username, String password, String name, boolean businessRegistered) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("username은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("password는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.username = username;
        this.password = password;
        this.name = name;
        this.businessRegistered=businessRegistered;
    }

    public boolean isSameId(Long userId) {
        return Objects.equals(this.id, userId);
    }
}
