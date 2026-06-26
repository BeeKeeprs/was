package kr.co.webee.domain.news.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class NewsArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String source;

    @Column(nullable = false, unique = true)
    private String originalLink;

    private LocalDateTime publishedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fetchedAt;

    @Builder
    private NewsArticle(String title, String summary, String content, String source,
                        String originalLink, LocalDateTime publishedAt, LocalDateTime fetchedAt) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(originalLink)) {
            throw new IllegalArgumentException("originalLink는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.source = source;
        this.originalLink = originalLink;
        this.publishedAt = publishedAt;
        this.fetchedAt = Objects.requireNonNull(fetchedAt, "fetchedAt은 null이 될 수 없습니다.");
    }
}
