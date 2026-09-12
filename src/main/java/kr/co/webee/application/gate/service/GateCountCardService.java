package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateCountCardRequest;
import kr.co.webee.application.gate.dto.response.GateCountCardResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.entity.GateCountCard;
import kr.co.webee.domain.gate.repository.GateCountCardRepository;
import kr.co.webee.domain.gate.repository.GateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GateCountCardService {

    private final GateRepository gateRepository;
    private final GateCountCardRepository gateCountCardRepository;

    @Transactional
    public GateCountCardResponse createCard(Long gateId, Long userId, GateCountCardRequest request) {
        Gate gate = findGateByIdAndUserId(gateId, userId);
        GateCountCard card = gateCountCardRepository.save(request.toEntity(gate));
        return GateCountCardResponse.from(card);
    }

    @Transactional(readOnly = true)
    public List<GateCountCardResponse> getCards(Long gateId, Long userId) {
        findGateByIdAndUserId(gateId, userId);
        return gateCountCardRepository.findAllByGateId(gateId).stream()
                .map(GateCountCardResponse::from)
                .toList();
    }

    @Transactional
    public GateCountCardResponse updateCard(Long gateId, Long userId, Long cardId, GateCountCardRequest request) {
        findGateByIdAndUserId(gateId, userId);
        GateCountCard card = gateCountCardRepository.findByIdAndGateId(cardId, gateId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_COUNT_CARD_NOT_FOUND));
        card.update(request.repeatDays(), request.minCount(), request.maxCount(),
                request.startHour(), request.endHour(),
                request.withinEntranceOpen(), request.withinExitOpen(),
                request.aboveEntranceOpen(), request.aboveExitOpen(),
                request.memo());
        return GateCountCardResponse.from(card);
    }

    @Transactional
    public void deleteCard(Long gateId, Long userId, Long cardId) {
        findGateByIdAndUserId(gateId, userId);
        GateCountCard card = gateCountCardRepository.findByIdAndGateId(cardId, gateId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_COUNT_CARD_NOT_FOUND));
        gateCountCardRepository.delete(card);
    }

    private Gate findGateByIdAndUserId(Long gateId, Long userId) {
        return gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));
    }
}
