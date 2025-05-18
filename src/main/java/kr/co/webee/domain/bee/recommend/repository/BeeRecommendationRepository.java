package kr.co.webee.domain.bee.recommend.repository;

import kr.co.webee.domain.bee.recommend.entity.BeeRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeeRecommendationRepository extends JpaRepository<BeeRecommendation, Long> {
}
