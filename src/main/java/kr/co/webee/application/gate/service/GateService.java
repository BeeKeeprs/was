package kr.co.webee.application.gate.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.repository.GateRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.gate.dto.request.GateRegisterRequest;
import kr.co.webee.presentation.gate.dto.response.GateRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GateService {
    private final GateRepository gateRepository;
    private final UserRepository userRepository;

    @Transactional
    public GateRegisterResponse registerGate(GateRegisterRequest request, Long userId) {
        if (gateRepository.existsByMacAddress(request.macAddress())) {
            throw new BusinessException(ErrorType.GATE_MAC_ADDRESS_ALREADY_EXISTS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        Gate gate = gateRepository.save(request.toEntity(user));

        return GateRegisterResponse.of(gate.getId());
    }
}
