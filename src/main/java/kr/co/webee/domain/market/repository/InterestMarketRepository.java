package kr.co.webee.domain.market.repository;

import kr.co.webee.domain.market.entity.InterestMarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestMarketRepository extends JpaRepository<InterestMarket, Long> {

    List<InterestMarket> findByUserId(Long userId);

    @Query("""
            select count(im) > 0
            from InterestMarket im
            where im.user.id = :userId
              and im.marketCode = :marketCode
            """)
    boolean existsBy(Long userId, String marketCode);

    @Query("""
            select count(im) > 0
            from InterestMarket im
            where im.user.id = :userId
              and im.marketCode = :marketCode
              and im.cropMajorCode = :cropMajorCode
              and im.cropMidName = :cropMidName
              and im.cropMinorName = :cropMinorName
            """)
    boolean existsBy(Long userId, String marketCode, String cropMajorCode, String cropMidName, String cropMinorName);

    Optional<InterestMarket> findByIdAndUserId(Long id, Long userId);
}

