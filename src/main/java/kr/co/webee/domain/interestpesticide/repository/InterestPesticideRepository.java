package kr.co.webee.domain.interestpesticide.repository;

import kr.co.webee.domain.interestpesticide.entity.InterestPesticide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@Repository
public interface InterestPesticideRepository extends JpaRepository<InterestPesticide, Long> {

    Slice<InterestPesticide> findByUserId(Long userId, Pageable pageable);

    boolean existsByUserIdAndPesticideApplicationNo(Long userId, String pesticideApplicationNo);

    Optional<InterestPesticide> findByIdAndUserId(Long id, Long userId);
}
