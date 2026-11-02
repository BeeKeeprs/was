package kr.co.webee.domain.gate.repository;

import kr.co.webee.domain.gate.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {
    boolean existsByMacAddress(String macAddress);

    Optional<Gate> findByMacAddress(String macAddress);

    Optional<Gate> findByIdAndUserId(Long id, Long userId);

    @Query("""
            select g
            from Gate g
            left join fetch g.hive
            where g.user.id = :userId
            """)
    List<Gate> findAllByUserIdWithHive(@Param("userId") Long userId);
}
