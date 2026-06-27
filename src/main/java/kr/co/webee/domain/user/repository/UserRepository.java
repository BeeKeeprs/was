package kr.co.webee.domain.user.repository;

import kr.co.webee.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    List<User> findByLastActivityAtAfter(LocalDateTime since);
}
