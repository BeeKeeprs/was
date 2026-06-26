package kr.co.webee.domain.news.repository;

import kr.co.webee.domain.news.entity.NewsArticle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    boolean existsByOriginalLink(String originalLink);
    Optional<NewsArticle> findByOriginalLink(String originalLink);

    @Query("""
            SELECT a
            FROM NewsArticle a
            JOIN NewsArticleKeyword k ON k.newsArticle = a
            WHERE k.keyword = :keyword
            ORDER BY a.publishedAt DESC, a.id DESC
            """)
    Slice<NewsArticle> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
