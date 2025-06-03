package kr.co.webee.domain.bee.diagnosis.repository;

import kr.co.webee.domain.bee.diagnosis.entity.BeeDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeeDiagnosisRepository extends JpaRepository<BeeDiagnosis, Long> {
}
