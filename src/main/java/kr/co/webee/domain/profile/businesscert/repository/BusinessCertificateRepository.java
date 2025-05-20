package kr.co.webee.domain.profile.businesscert.repository;

import kr.co.webee.domain.profile.businesscert.entity.BusinessCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessCertificateRepository extends JpaRepository<BusinessCertificate, Long> {
}
