package kr.co.webee.domain.news.entity;

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
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class NewsArticleKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_article_id", nullable = false)
    private NewsArticle newsArticle;

    @Column(nullable = false)
    private String keyword;

    public static NewsArticleKeyword create(NewsArticle newsArticle, String keyword) {
        return NewsArticleKeyword.builder()
                .newsArticle(newsArticle)
                .keyword(keyword)
                .build();
    }

    @Builder
    private NewsArticleKeyword(NewsArticle newsArticle, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("keyword는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.newsArticle = Objects.requireNonNull(newsArticle, "newsArticle은 null이 될 수 없습니다.");
        this.keyword = keyword;
    }
}
