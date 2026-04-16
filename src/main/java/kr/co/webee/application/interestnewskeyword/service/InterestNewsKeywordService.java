package kr.co.webee.application.interestnewskeyword.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.interestnewskeyword.entity.InterestNewsKeyword;
import kr.co.webee.domain.interestnewskeyword.repository.InterestNewsKeywordRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.interestnewskeyword.dto.request.InterestNewsKeywordRegisterRequest;
import kr.co.webee.presentation.interestnewskeyword.dto.response.InterestNewsKeywordRegisterResponse;
import kr.co.webee.presentation.interestnewskeyword.dto.response.InterestNewsKeywordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestNewsKeywordService {
    private final InterestNewsKeywordRepository interestNewsKeywordRepository;
    private final UserRepository userRepository;

    @Transactional
    public InterestNewsKeywordRegisterResponse registerInterestNewsKeyword(InterestNewsKeywordRegisterRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.ENTITY_NOT_FOUND));

        InterestNewsKeyword interestNewsKeyword = interestNewsKeywordRepository.save(request.toEntity(user));
        return InterestNewsKeywordRegisterResponse.of(interestNewsKeyword.getId());
    }

    @Transactional(readOnly = true)
    public List<InterestNewsKeywordResponse> getInterestNewsKeywords(Long userId) {
        return interestNewsKeywordRepository.findByUserId(userId).stream()
                .map(InterestNewsKeywordResponse::from)
                .toList();
    }
}

