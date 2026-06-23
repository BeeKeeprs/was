package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveReplacementHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface HiveReplacementHistoryRepository extends JpaRepository<HiveReplacementHistory, Long> {

    Slice<HiveReplacementHistory> findAllByHiveId(Long hiveId, Pageable pageable);

    Optional<HiveReplacementHistory> findByIdAndHiveId(Long id, Long hiveId);

    @Query("""
            SELECT h FROM HiveReplacementHistory h
            WHERE h.hive.id = :hiveId
            ORDER BY h.replacedAt DESC
            LIMIT 1
            """)
    Optional<HiveReplacementHistory> findLatestByHiveId(Long hiveId);

    @Query("""
            SELECT h FROM HiveReplacementHistory h
            WHERE h.hive.id = :hiveId AND h.replacedAt > :replacedAt
            ORDER BY h.replacedAt ASC
            LIMIT 1
            """)
    Optional<HiveReplacementHistory> findNewerByHiveId(Long hiveId, LocalDate replacedAt);

    @Query("""
            SELECT h FROM HiveReplacementHistory h
            WHERE h.hive.id = :hiveId AND h.replacedAt < :replacedAt
            ORDER BY h.replacedAt DESC
            LIMIT 1
            """)
    Optional<HiveReplacementHistory> findOlderByHiveId(Long hiveId, LocalDate replacedAt);

    @Modifying
    @Query("DELETE FROM HiveReplacementHistory h WHERE h.hive.id = :hiveId")
    void deleteAllByHiveId(@Param("hiveId") Long hiveId);
}
