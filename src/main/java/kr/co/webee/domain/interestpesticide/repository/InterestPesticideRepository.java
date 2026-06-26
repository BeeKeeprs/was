package kr.co.webee.domain.interestpesticide.repository;

import kr.co.webee.domain.interestpesticide.entity.InterestPesticide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestPesticideRepository extends JpaRepository<InterestPesticide, Long> {

    List<InterestPesticide> findByUserId(Long userId);

    boolean existsByUserIdAndPesticideApplicationNo(Long userId, String pesticideApplicationNo);

    Optional<InterestPesticide> findByIdAndUserId(Long id, Long userId);
}
