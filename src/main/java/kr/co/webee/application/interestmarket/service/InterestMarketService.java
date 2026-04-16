package kr.co.webee.application.interestmarket.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.interestmarket.entity.InterestMarket;
import kr.co.webee.domain.interestmarket.repository.InterestMarketRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.interestmarket.dto.request.InterestMarketRegisterRequest;
import kr.co.webee.presentation.interestmarket.dto.response.InterestMarketRegisterResponse;
import kr.co.webee.presentation.interestmarket.dto.response.InterestMarketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestMarketService {
    private final InterestMarketRepository interestMarketRepository;
    private final UserRepository userRepository;

    @Transactional
    public InterestMarketRegisterResponse registerInterestMarket(InterestMarketRegisterRequest request, Long userId) {
        validateDuplicateInterestMarket(request, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.ENTITY_NOT_FOUND));

        InterestMarket interestMarket = interestMarketRepository.save(request.toEntity(user));
        return InterestMarketRegisterResponse.of(interestMarket.getId());
    }

    @Transactional(readOnly = true)
    public List<InterestMarketResponse> getAllInterestMarkets(Long userId) {
        return interestMarketRepository.findByUserId(userId).stream()
                .map(InterestMarketResponse::from)
                .toList();
    }

    @Transactional
    public void removeInterestMarket(Long interestMarketId, Long userId) {
        InterestMarket entity = interestMarketRepository.findByIdAndUserId(interestMarketId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.INTEREST_MARKET_NOT_FOUND));

        interestMarketRepository.delete(entity);
    }

    private void validateDuplicateInterestMarket(InterestMarketRegisterRequest request, Long userId) {
        boolean exists;

        if (request.isMarketOnly()) {
            exists = interestMarketRepository.existsBy(userId, request.marketCode());
        } else if (request.isMarketWithCrop()) {
            exists = interestMarketRepository.existsBy(userId, request.marketCode(), request.cropMajorCode(), request.cropMidName(), request.cropMinorName());
        } else {
            throw new BusinessException(ErrorType.FAILED_VALIDATION);
        }

        if (exists) {
            throw new BusinessException(ErrorType.INTEREST_MARKET_ALREADY_EXISTS);
        }
    }
}

