package kr.co.webee.domain.user.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    private String password;

    @Column(nullable = false)
    private String name;

    private String phoneNumber;

    @ColumnDefault("false")
    private boolean businessRegistered;

    private String profileImageUrl;

    private LocalDateTime deletedAt;

    @Builder
    public User(String username, String password, String name, String phoneNumber, boolean businessRegistered) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("username은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber=phoneNumber;
        this.businessRegistered=businessRegistered;
    }

    public void updatePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            throw new IllegalArgumentException("phoneNumber은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.phoneNumber = phoneNumber;
    }

    public boolean isSameId(Long userId) {
        return Objects.equals(this.id, userId);
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
