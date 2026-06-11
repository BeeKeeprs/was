package kr.co.webee.domain.notification.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.notification.type.NotificationType;
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private LocalDateTime sentAt;

    @Column(nullable = false)
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Builder
    private Notification(User user, NotificationType type, String title, String content, LocalDateTime sentAt, boolean isRead) {
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
        this.type = Objects.requireNonNull(type, "type은 null이 될 수 없습니다.");
        this.title = Objects.requireNonNull(title, "title은 null이 될 수 없습니다.");
        this.content = Objects.requireNonNull(content, "content는 null이 될 수 없습니다.");
        this.sentAt = Objects.requireNonNull(sentAt, "sentAt는 null이 될 수 없습니다.");
        this.isRead = isRead;
    }

    public static Notification create(User user, NotificationType type, String title, String content) {
        return Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .content(content)
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();
    }
}
