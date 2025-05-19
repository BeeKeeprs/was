package kr.co.webee.domain.profile.crop.repository;

import kr.co.webee.domain.profile.crop.entity.UserCrop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCropRepository extends JpaRepository<UserCrop, Long> {
    List<UserCrop> findByUserId(Long userId);

    @Query("""
            SELECT DISTINCT uc.cultivationLocation.address
            FROM UserCrop uc
            WHERE uc.user.id = :userId
            """)
    List<String> findAddressesByUserId(@Param("userId") Long userId);
}
