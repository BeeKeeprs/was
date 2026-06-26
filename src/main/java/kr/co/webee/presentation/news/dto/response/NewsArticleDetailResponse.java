package kr.co.webee.presentation.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.news.entity.NewsArticle;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "뉴스 기사 상세 response")
public record NewsArticleDetailResponse(
        @Schema(description = "뉴스 기사 ID", example = "101")
        Long newsArticleId,

        @Schema(description = "제목", example = "수정벌 방사 시기, 기온 체크가 관건")
        String title,

        @Schema(description = "내용", example = "과수 농가에서 수정벌 방사 전 확인해야 할 사항...")
        String content,

        @Schema(description = "출처", example = "농민신문")
        String source,

        @Schema(description = "발행 일시", example = "2026-06-16T09:00:00")
        LocalDateTime publishedAt
) {
    public static NewsArticleDetailResponse from(NewsArticle article) {
        return NewsArticleDetailResponse.builder()
                .newsArticleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .source(article.getSource())
                .publishedAt(article.getPublishedAt())
                .build();
    }
}
