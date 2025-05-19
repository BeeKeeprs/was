package kr.co.webee.domain.bee.recommend.repository;

import io.lettuce.core.dynamic.annotation.Param;
import kr.co.webee.domain.bee.recommend.entity.BeeRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeeRecommendationRepository extends JpaRepository<BeeRecommendation, Long> {
    @Query("""
            SELECT br FROM BeeRecommendation br
            JOIN FETCH br.userCrop uc
            WHERE uc.user.id = :userId
            """)
    List<BeeRecommendation> findByUserId(@Param("userId") Long userId);
}
