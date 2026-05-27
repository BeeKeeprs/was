package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveBeeCountRequest;
import kr.co.webee.application.hive.dto.response.HiveBeeCountResponse;
import kr.co.webee.application.hive.dto.response.HiveBeeCountResponse.DataPoint;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveBeeCount;
import kr.co.webee.domain.hive.repository.HiveBeeCountRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.type.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public HiveBeeCountResponse getBeeCount(Long hiveId, Long userId, Period period) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = period.startFrom(now);

        List<HiveBeeCount> beeCounts = hiveBeeCountRepository.findByHiveIdAndRecordedAtBetween(hiveId, start, now);

        Map<LocalDateTime, List<Integer>> grouped = beeCounts.stream()
                .collect(Collectors.groupingBy(
                        b -> period.truncate(b.getRecordedAt()),
                        Collectors.mapping(HiveBeeCount::getCount, Collectors.toList())
                ));

        List<DataPoint> data = generateSlots(start, now, period).stream()
                .map(slot -> DataPoint.of(
                        period.formatLabel(slot),
                        calculateAverage(slot, grouped)
                ))
                .toList();

        return HiveBeeCountResponse.of(period, data);
    }

    private List<LocalDateTime> generateSlots(LocalDateTime start, LocalDateTime end, Period period) {
        List<LocalDateTime> slots = new ArrayList<>();

        LocalDateTime current = period.truncate(start);

        while (!current.isAfter(end)) {
            slots.add(current);
            current = period.next(current);
        }

        return slots;
    }

    private Integer calculateAverage(LocalDateTime slot, Map<LocalDateTime, List<Integer>> grouped) {
        List<Integer> values = grouped.getOrDefault(slot, List.of());

        return values.isEmpty() ? null
                : (int) Math.round(values.stream().mapToInt(Integer::intValue).average().orElse(0.0));
    }
}
