package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveGateActionRegisterRequest;
import kr.co.webee.application.hive.dto.response.HiveGateActionListResponse;
import kr.co.webee.application.hive.dto.response.HiveGateActionRegisterResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveGateAction;
import kr.co.webee.domain.hive.repository.HiveGateActionRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HiveGateActionService {

    private final HiveGateActionRepository hiveGateActionRepository;
    private final HiveRepository hiveRepository;

    @Transactional
    public HiveGateActionRegisterResponse registerHiveGateAction(Long hiveId, Long userId, HiveGateActionRegisterRequest request) {
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        HiveGateAction hiveGateAction = hiveGateActionRepository.save(request.toEntity(hive));

        return HiveGateActionRegisterResponse.of(hiveGateAction);
    }

    @Transactional(readOnly = true)
    public List<HiveGateActionListResponse> getAllHiveGateActionList(Long hiveId, Long userId) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        return hiveGateActionRepository.findAllByHiveId(hiveId).stream()
                .map(HiveGateActionListResponse::from)
                .toList();
    }
}