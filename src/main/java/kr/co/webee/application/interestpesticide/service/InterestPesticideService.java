package kr.co.webee.application.interestpesticide.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.interestpesticide.entity.InterestPesticide;
import kr.co.webee.domain.interestpesticide.repository.InterestPesticideRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.interestpesticide.dto.request.InterestPesticideRegisterRequest;
import kr.co.webee.presentation.interestpesticide.dto.response.InterestPesticideRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestPesticideService {
    private final InterestPesticideRepository interestPesticideRepository;
    private final UserRepository userRepository;

    @Transactional
    public InterestPesticideRegisterResponse registerInterestPesticide(InterestPesticideRegisterRequest request, Long userId) {
        if (interestPesticideRepository.existsByUserIdAndPesticideApplicationNo(userId, request.pesticideApplicationNo())) {
            throw new BusinessException(ErrorType.INTEREST_PESTICIDE_ALREADY_EXISTS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        InterestPesticide interestPesticide = interestPesticideRepository.save(request.toEntity(user));
        return InterestPesticideRegisterResponse.of(interestPesticide.getId());
    }
}
