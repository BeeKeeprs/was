package kr.co.webee.domain.product.repository;

import kr.co.webee.domain.product.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    @Query("""
                SELECT pr FROM ProductReview pr
                JOIN FETCH pr.user u
                WHERE pr.product.id = :productId
            """)
    List<ProductReview> findAllByProductId(Long productId);
}
