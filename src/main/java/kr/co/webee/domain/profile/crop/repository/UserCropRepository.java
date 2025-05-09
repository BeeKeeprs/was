package kr.co.webee.domain.profile.crop.repository;

import kr.co.webee.domain.profile.crop.entity.UserCrop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCropRepository extends JpaRepository<UserCrop, Long> {
}
