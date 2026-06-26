package kr.co.webee.domain.interestnewskeyword.repository;

import kr.co.webee.domain.interestnewskeyword.entity.InterestNewsKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestNewsKeywordRepository extends JpaRepository<InterestNewsKeyword, Long> {
    List<InterestNewsKeyword> findByUserId(Long userId);

    @Query("SELECT DISTINCT k.keyword FROM InterestNewsKeyword k")
    List<String> findAllDistinctKeywords();
}

