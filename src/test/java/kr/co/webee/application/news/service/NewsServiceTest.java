package kr.co.webee.application.news.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.news.entity.NewsArticle;
import kr.co.webee.domain.news.repository.NewsArticleKeywordRepository;
import kr.co.webee.domain.news.repository.NewsArticleRepository;
import kr.co.webee.infrastructure.news.dto.NaverNewsResponse;
import kr.co.webee.presentation.news.dto.response.NewsArticleDetailResponse;
import kr.co.webee.presentation.news.dto.response.NewsArticleListResponse;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class NewsServiceTest {

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsArticleRepository newsArticleRepository;

    @Autowired
    private NewsArticleKeywordRepository newsArticleKeywordRepository;

    @BeforeEach
    void setUp() {
        newsArticleKeywordRepository.deleteAllInBatch();
        newsArticleRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("뉴스 기사 목록 조회")
    class GetAllNewsArticleList {

        @Test
        @DisplayName("키워드에 해당하는 뉴스 기사 목록을 반환한다.")
        void getAllNewsArticleList() {
            //given
            NewsArticle article1 = newsArticleRepository.save(
                    TestFixture.createNewsArticle("꿀벌 기사1", "https://news.example.com/1"));
            NewsArticle article2 = newsArticleRepository.save(
                    TestFixture.createNewsArticle("꿀벌 기사2", "https://news.example.com/2"));
            newsArticleKeywordRepository.save(TestFixture.createNewsArticleKeyword(article1, "꿀벌"));
            newsArticleKeywordRepository.save(TestFixture.createNewsArticleKeyword(article2, "꿀벌"));

            //when
            Slice<NewsArticleListResponse> result = newsService.getAllNewsArticleList("꿀벌", PageRequest.of(0, 10));

            //then
            assertThat(result.getContent()).hasSize(2)
                    .extracting("title")
                    .containsExactlyInAnyOrder("꿀벌 기사1", "꿀벌 기사2");
        }

        @Test
        @DisplayName("해당 키워드의 기사가 없으면 빈 목록을 반환한다.")
        void getAllNewsArticleListEmpty() {
            //when
            Slice<NewsArticleListResponse> result = newsService.getAllNewsArticleList("꿀벌", PageRequest.of(0, 10));

            //then
            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("뉴스 기사 상세 조회")
    class GetNewsArticleDetail {

        @Test
        @DisplayName("뉴스 기사 상세를 조회한다.")
        void getNewsArticleDetail() {
            //given
            NewsArticle article = newsArticleRepository.save(
                    TestFixture.createNewsArticle("수정벌 방사 시기, 기온 체크가 관건", "https://news.example.com/1"));

            //when
            NewsArticleDetailResponse result = newsService.getNewsArticleDetail(article.getId());

            //then
            assertThat(result.newsArticleId()).isEqualTo(article.getId());
            assertThat(result.title()).isEqualTo("수정벌 방사 시기, 기온 체크가 관건");
            assertThat(result.source()).isEqualTo("테스트 언론사");
        }

        @Test
        @DisplayName("존재하지 않는 뉴스 기사를 조회하면 예외가 발생한다.")
        void getNewsArticleDetailNotFound() {
            //when - then
            assertThatThrownBy(() -> newsService.getNewsArticleDetail(-1L))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.ENTITY_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("키워드별 뉴스 기사 저장")
    class AddNewsArticleByKeyword {

        private List<NaverNewsResponse.Item> singleItem(String title, String originalLink) {
            return List.of(new NaverNewsResponse.Item(
                    title, "테스트 요약", originalLink, originalLink,
                    "Tue, 16 Jun 2026 09:00:00 +0900"));
        }

        @Test
        @DisplayName("새로운 기사이면 저장한다.")
        void addNewsArticleByKeyword_savesNewArticle() {
            //when
            newsService.addNewsArticleByKeyword("꿀벌", singleItem("꿀벌 기사", "https://news.example.com/1"));

            //then
            assertThat(newsArticleRepository.count()).isEqualTo(1);
            assertThat(newsArticleKeywordRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("이미 존재하는 기사에 연결되지 않은 키워드이면 키워드를 추가한다.")
        void addNewsArticleByKeyword_addsKeywordToExistingArticle() {
            //given
            NewsArticle article = newsArticleRepository.save(
                    TestFixture.createNewsArticle("꿀벌 기사", "https://news.example.com/1"));
            newsArticleKeywordRepository.save(TestFixture.createNewsArticleKeyword(article, "꿀벌"));

            //when
            newsService.addNewsArticleByKeyword("양봉", singleItem("꿀벌 기사", "https://news.example.com/1"));

            //then
            assertThat(newsArticleRepository.count()).isEqualTo(1);
            assertThat(newsArticleKeywordRepository.count()).isEqualTo(2);
        }

        @Test
        @DisplayName("이미 연결된 키워드는 중복 저장하지 않는다.")
        void addNewsArticleByKeyword_doesNotDuplicateKeyword() {
            //given
            NewsArticle article = newsArticleRepository.save(
                    TestFixture.createNewsArticle("꿀벌 기사", "https://news.example.com/1"));
            newsArticleKeywordRepository.save(TestFixture.createNewsArticleKeyword(article, "꿀벌"));

            //when
            newsService.addNewsArticleByKeyword("꿀벌", singleItem("꿀벌 기사", "https://news.example.com/1"));

            //then
            assertThat(newsArticleKeywordRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("빈 목록이면 저장하지 않는다.")
        void addNewsArticleByKeyword_emptyItems() {
            //when
            newsService.addNewsArticleByKeyword("꿀벌", List.of());

            //then
            assertThat(newsArticleRepository.count()).isEqualTo(0);
            assertThat(newsArticleKeywordRepository.count()).isEqualTo(0);
        }
    }
}
