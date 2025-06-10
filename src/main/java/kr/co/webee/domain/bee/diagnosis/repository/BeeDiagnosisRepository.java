package kr.co.webee.domain.bee.diagnosis.repository;

import kr.co.webee.domain.bee.diagnosis.entity.BeeDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeeDiagnosisRepository extends JpaRepository<BeeDiagnosis, Long> {
    List<BeeDiagnosis> findByUserId(Long userId);
}
