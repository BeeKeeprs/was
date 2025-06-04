package kr.co.webee.domain.profile.business.repository;

import kr.co.webee.domain.profile.business.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByUserId(Long userId);
}
