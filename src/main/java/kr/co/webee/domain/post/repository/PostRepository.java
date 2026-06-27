package kr.co.webee.domain.post.repository;

import kr.co.webee.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
            SELECT new kr.co.webee.domain.post.repository.CategoryCount(p.category, COUNT(p))
            FROM Post p
            WHERE p.createdAt >= :since
            GROUP BY p.category
            ORDER BY COUNT(p) DESC
            """)
    List<CategoryCount> findTopCategoriesSince(LocalDateTime since, Pageable pageable);
}
