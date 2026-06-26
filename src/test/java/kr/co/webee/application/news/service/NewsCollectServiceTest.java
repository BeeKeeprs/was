package kr.co.webee.application.news.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.domain.news.repository.NewsArticleKeywordRepository;
import kr.co.webee.domain.news.repository.NewsArticleRepository;
import kr.co.webee.infrastructure.news.client.NewsClient;
import kr.co.webee.infrastructure.news.dto.NaverNewsResponse;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@IntegrationTest
class NewsCollectServiceTest {

    @Autowired
    private NewsCollectService newsCollectService;

    @Autowired
    private NewsArticleRepository newsArticleRepository;

    @Autowired
    private NewsArticleKeywordRepository newsArticleKeywordRepository;

    @MockitoBean
    private NewsClient newsClient;

    @BeforeEach
    void setUp() {
        newsArticleKeywordRepository.deleteAllInBatch();
        newsArticleRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("뉴스 기사 수집")
    class CollectNewsArticles {

        @Test
        @DisplayName("기본 키워드에 해당하는 기사를 수집하여 저장한다.")
        void collectNewsArticles_savesArticlesForDefaultKeywords() {
            //given
            when(newsClient.fetchNews(anyString())).thenReturn(new NaverNewsResponse(0, 0, List.of()));
            when(newsClient.fetchNews(eq("꿀벌"))).thenReturn(new NaverNewsResponse(1, 1, List.of(
                    new NaverNewsResponse.Item("꿀벌 기사", "테스트 요약",
                            "https://news.example.com/1", "https://news.example.com/1",
                            "Tue, 16 Jun 2026 09:00:00 +0900")
            )));

            //when
            newsCollectService.collectNewsArticles();

            //then
            assertThat(newsArticleRepository.count()).isEqualTo(1);
            assertThat(newsArticleKeywordRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("빈 응답이면 기사를 저장하지 않는다.")
        void collectNewsArticles_emptyResponse() {
            //given
            when(newsClient.fetchNews(anyString())).thenReturn(new NaverNewsResponse(0, 0, List.of()));

            //when
            newsCollectService.collectNewsArticles();

            //then
            assertThat(newsArticleRepository.count()).isEqualTo(0);
            assertThat(newsArticleKeywordRepository.count()).isEqualTo(0);
        }
    }
}
