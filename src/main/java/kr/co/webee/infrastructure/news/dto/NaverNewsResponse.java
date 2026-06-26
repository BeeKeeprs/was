package kr.co.webee.infrastructure.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.webee.domain.news.entity.NewsArticle;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public record NaverNewsResponse(
        int total,
        int display,
        List<Item> items
) {
    public record Item(
            String title,
            String description,
            @JsonProperty("originallink") String originalLink,
            String link,
            String pubDate
    ) {
        public NewsArticle toEntity(LocalDateTime fetchedAt) {
            return NewsArticle.builder()
                    .title(stripHtml(title))
                    .summary(stripHtml(description))
                    .originalLink(originalLink)
                    .publishedAt(parseToLocalDateTime(pubDate))
                    .fetchedAt(fetchedAt)
                    .build();
        }

        private LocalDateTime parseToLocalDateTime(String pubDate) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(pubDate, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH));
            return zonedDateTime.toLocalDateTime();
        }

        private static String stripHtml(String html) {
            return html.replaceAll("<[^>]+>", "")
                    .replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&quot;", "\"")
                    .replace("&#39;", "'");
        }
    }
}
