package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateTimeCardRequest;
import kr.co.webee.application.gate.dto.response.GateTimeCardResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.entity.GateTimeCard;
import kr.co.webee.domain.gate.repository.GateRepository;
import kr.co.webee.domain.gate.repository.GateTimeCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GateTimeCardService {

    private final GateRepository gateRepository;
    private final GateTimeCardRepository gateTimeCardRepository;

    @Transactional
    public GateTimeCardResponse createCard(Long gateId, Long userId, GateTimeCardRequest request) {
        Gate gate = findGateByIdAndUserId(gateId, userId);
        GateTimeCard card = gateTimeCardRepository.save(request.toEntity(gate));
        return GateTimeCardResponse.from(card);
    }

    @Transactional(readOnly = true)
    public List<GateTimeCardResponse> getCards(Long gateId, Long userId) {
        findGateByIdAndUserId(gateId, userId);
        return gateTimeCardRepository.findAllByGateId(gateId).stream()
                .map(GateTimeCardResponse::from)
                .toList();
    }

    @Transactional
    public GateTimeCardResponse updateCard(Long gateId, Long userId, Long cardId, GateTimeCardRequest request) {
        findGateByIdAndUserId(gateId, userId);
        GateTimeCard card = gateTimeCardRepository.findByIdAndGateId(cardId, gateId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_TIME_CARD_NOT_FOUND));
        card.update(request.actionType(), request.startHour(), request.endHour(), request.repeatEnabled(), request.memo());
        return GateTimeCardResponse.from(card);
    }

    @Transactional
    public void deleteCard(Long gateId, Long userId, Long cardId) {
        findGateByIdAndUserId(gateId, userId);
        GateTimeCard card = gateTimeCardRepository.findByIdAndGateId(cardId, gateId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_TIME_CARD_NOT_FOUND));
        gateTimeCardRepository.delete(card);
    }

    private Gate findGateByIdAndUserId(Long gateId, Long userId) {
        return gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));
    }
}
