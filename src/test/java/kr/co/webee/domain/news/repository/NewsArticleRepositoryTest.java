package kr.co.webee.domain.news.repository;

import kr.co.webee.domain.annotation.RepositoryTest;
import kr.co.webee.domain.news.entity.NewsArticle;
import kr.co.webee.domain.news.entity.NewsArticleKeyword;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class NewsArticleRepositoryTest {

    @Autowired
    private NewsArticleRepository newsArticleRepository;

    @Autowired
    private NewsArticleKeywordRepository newsArticleKeywordRepository;

    @Nested
    @DisplayName("키워드로 뉴스 기사 목록 조회")
    class FindAllByKeyword {

        @Test
        @DisplayName("키워드에 해당하는 뉴스 기사 목록을 발행일 내림차순으로 반환한다.")
        void findAllByKeyword() {
            //given
            NewsArticle article1 = newsArticleRepository.save(
                    TestFixture.createNewsArticle("꿀벌 기사1", "https://news.example.com/1"));
            NewsArticle article2 = newsArticleRepository.save(
                    TestFixture.createNewsArticle("꿀벌 기사2", "https://news.example.com/2"));
            newsArticleKeywordRepository.save(TestFixture.createNewsArticleKeyword(article1, "꿀벌"));
            newsArticleKeywordRepository.save(TestFixture.createNewsArticleKeyword(article2, "꿀벌"));

            //when
            Slice<NewsArticle> result = newsArticleRepository.findAllByKeyword("꿀벌", PageRequest.of(0, 10));

            //then
            assertThat(result.getContent()).hasSize(2)
                    .extracting("title")
                    .containsExactlyInAnyOrder("꿀벌 기사1", "꿀벌 기사2");
        }

        @Test
        @DisplayName("다른 키워드의 기사는 조회되지 않는다.")
        void findAllByKeywordOtherKeywordExcluded() {
            //given
            NewsArticle article = newsArticleRepository.save(
                    TestFixture.createNewsArticle("양봉 기사", "https://news.example.com/3"));
            newsArticleKeywordRepository.save(TestFixture.createNewsArticleKeyword(article, "양봉"));

            //when
            Slice<NewsArticle> result = newsArticleRepository.findAllByKeyword("꿀벌", PageRequest.of(0, 10));

            //then
            assertThat(result.getContent()).isEmpty();
        }

        @Test
        @DisplayName("해당 키워드의 기사가 없으면 빈 결과를 반환한다.")
        void findAllByKeywordEmpty() {
            //when
            Slice<NewsArticle> result = newsArticleRepository.findAllByKeyword("꿀벌", PageRequest.of(0, 10));

            //then
            assertThat(result.getContent()).isEmpty();
            assertThat(result.hasNext()).isFalse();
        }

        @Test
        @DisplayName("size보다 많은 기사가 있으면 hasNext가 true이다.")
        void findAllByKeywordHasNext() {
            //given
            for (int i = 1; i <= 3; i++) {
                NewsArticle article = newsArticleRepository.save(
                        TestFixture.createNewsArticle("꿀벌 기사" + i, "https://news.example.com/" + i));
                newsArticleKeywordRepository.save(TestFixture.createNewsArticleKeyword(article, "꿀벌"));
            }

            //when
            Slice<NewsArticle> result = newsArticleRepository.findAllByKeyword("꿀벌", PageRequest.of(0, 2));

            //then
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.hasNext()).isTrue();
        }
    }
}
