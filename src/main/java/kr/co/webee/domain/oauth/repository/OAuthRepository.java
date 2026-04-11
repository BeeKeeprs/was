package kr.co.webee.domain.oauth.repository;

import kr.co.webee.domain.oauth.entity.OAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRepository extends JpaRepository<OAuth, Long> {
}
