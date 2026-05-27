package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveBeeCountRequest;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveBeeCount;
import kr.co.webee.domain.hive.repository.HiveBeeCountRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HiveBeeCountService {
    private final HiveBeeCountRepository hiveBeeCountRepository;
    private final HiveRepository hiveRepository;

    @Transactional
    public void recordBeeCount(HiveBeeCountRequest request, String macAddress) {
        Hive hive = hiveRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 벌통입니다. macAddress=" + macAddress));

        HiveBeeCount beeCount = request.toEntity(hive);

        hiveBeeCountRepository.save(beeCount);
    }
}
