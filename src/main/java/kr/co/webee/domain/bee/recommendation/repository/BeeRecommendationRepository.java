package kr.co.webee.domain.bee.recommendation.repository;

import io.lettuce.core.dynamic.annotation.Param;
import kr.co.webee.domain.bee.recommendation.entity.BeeRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeeRecommendationRepository extends JpaRepository<BeeRecommendation, Long> {
    List<BeeRecommendation> findByUserId(@Param("userId") Long userId);
}
