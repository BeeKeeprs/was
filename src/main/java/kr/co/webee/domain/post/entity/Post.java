package kr.co.webee.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Builder
    private Post(String title, String content, User user) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.title = title;
        this.content = content;
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
        this.likeCount = 0;
    }

    public void update(String title, String content) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.title = title;
        this.content = content;
    }

    public boolean isWrittenBy(Long userId) {
        return user.isSameId(userId);
    }

    public boolean isNotWrittenBy(Long userId) {
        return !isWrittenBy(userId);
    }
}
