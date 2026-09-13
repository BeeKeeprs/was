package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateCountCardRequest;
import kr.co.webee.application.gate.dto.response.GateCountCardResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.GateCountCard;
import kr.co.webee.domain.gate.repository.GateCountCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GateCountCardService {

    private final GateCountCardRepository gateCountCardRepository;

    @Transactional
    public GateCountCardResponse createCard(Long userId, GateCountCardRequest request) {
        GateCountCard card = gateCountCardRepository.save(request.toEntity(userId));
        return GateCountCardResponse.from(card);
    }

    @Transactional(readOnly = true)
    public List<GateCountCardResponse> getCards(Long userId) {
        return gateCountCardRepository.findAllByUserId(userId).stream()
                .map(GateCountCardResponse::from)
                .toList();
    }

    @Transactional
    public GateCountCardResponse updateCard(Long userId, Long cardId, GateCountCardRequest request) {
        GateCountCard card = gateCountCardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_COUNT_CARD_NOT_FOUND));
        card.update(request.repeatDays(), request.minCount(), request.maxCount(),
                request.startHour(), request.endHour(),
                request.withinEntranceOpen(), request.withinExitOpen(),
                request.aboveEntranceOpen(), request.aboveExitOpen(),
                request.memo());
        return GateCountCardResponse.from(card);
    }

    @Transactional
    public void deleteCard(Long userId, Long cardId) {
        GateCountCard card = gateCountCardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_COUNT_CARD_NOT_FOUND));
        gateCountCardRepository.delete(card);
    }
}
