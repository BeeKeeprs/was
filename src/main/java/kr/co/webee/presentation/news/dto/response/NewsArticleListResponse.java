package kr.co.webee.presentation.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.news.entity.NewsArticle;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "뉴스 기사 목록 항목 response")
public record NewsArticleListResponse(
        @Schema(description = "뉴스 기사 ID", example = "101")
        Long newsArticleId,

        @Schema(description = "제목", example = "수정벌 방사 시기, 기온 체크가 관건")
        String title,

        @Schema(description = "출처", example = "농민신문")
        String source,

        @Schema(description = "발행 일시", example = "2026-06-16T09:00:00")
        LocalDateTime publishedAt
) {
    public static NewsArticleListResponse from(NewsArticle article) {
        return NewsArticleListResponse.builder()
                .newsArticleId(article.getId())
                .title(article.getTitle())
                .source(article.getSource())
                .publishedAt(article.getPublishedAt())
                .build();
    }
}
