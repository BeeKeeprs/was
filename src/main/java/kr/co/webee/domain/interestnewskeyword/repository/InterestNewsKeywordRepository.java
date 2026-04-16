package kr.co.webee.domain.interestnewskeyword.repository;

import kr.co.webee.domain.interestnewskeyword.entity.InterestNewsKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestNewsKeywordRepository extends JpaRepository<InterestNewsKeyword, Long> {
    List<InterestNewsKeyword> findByUserId(Long userId);
}

