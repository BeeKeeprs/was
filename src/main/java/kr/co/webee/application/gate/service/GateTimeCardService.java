package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateTimeCardRequest;
import kr.co.webee.application.gate.dto.response.GateTimeCardResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.GateTimeCard;
import kr.co.webee.domain.gate.repository.GateTimeCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GateTimeCardService {

    private final GateTimeCardRepository gateTimeCardRepository;

    @Transactional
    public GateTimeCardResponse createCard(Long userId, GateTimeCardRequest request) {
        GateTimeCard card = gateTimeCardRepository.save(request.toEntity(userId));
        return GateTimeCardResponse.from(card);
    }

    @Transactional(readOnly = true)
    public List<GateTimeCardResponse> getCards(Long userId) {
        return gateTimeCardRepository.findAllByUserId(userId).stream()
                .map(GateTimeCardResponse::from)
                .toList();
    }

    @Transactional
    public GateTimeCardResponse updateCard(Long userId, Long cardId, GateTimeCardRequest request) {
        GateTimeCard card = gateTimeCardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_TIME_CARD_NOT_FOUND));
        card.update(request.actionType(), request.startHour(), request.endHour(), request.repeatEnabled(), request.memo());
        return GateTimeCardResponse.from(card);
    }

    @Transactional
    public void deleteCard(Long userId, Long cardId) {
        GateTimeCard card = gateTimeCardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_TIME_CARD_NOT_FOUND));
        gateTimeCardRepository.delete(card);
    }
}
