package kr.co.webee.domain.news.repository;

import kr.co.webee.domain.news.entity.NewsArticleKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsArticleKeywordRepository extends JpaRepository<NewsArticleKeyword, Long> {
    boolean existsByNewsArticleIdAndKeyword(Long newsArticleId, String keyword);
}
