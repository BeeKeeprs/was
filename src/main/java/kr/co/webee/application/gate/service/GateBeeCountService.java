package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateBeeCountRequest;
import kr.co.webee.application.gate.dto.response.GateBeeCountResponse;
import kr.co.webee.application.gate.dto.response.GateBeeCountResponse.DataPoint;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.entity.GateBeeCount;
import kr.co.webee.domain.gate.repository.GateBeeCountRepository;
import kr.co.webee.domain.gate.repository.GateRepository;
import kr.co.webee.domain.hive.type.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GateBeeCountService {
    private final GateRepository gateRepository;
    private final GateBeeCountRepository gateBeeCountRepository;

    @Transactional
    public void recordBeeCount(GateBeeCountRequest request, String macAddress) {
        Gate gate = gateRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 개폐기입니다. macAddress=" + macAddress));

        gateBeeCountRepository.save(request.toEntity(gate));
    }

    @Transactional(readOnly = true)
    public GateBeeCountResponse getBeeCount(Long gateId, Long userId, Period period) {
        gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = period.startFrom(now);

        List<GateBeeCount> beeCounts = gateBeeCountRepository.findByGateIdAndRecordedAtBetween(gateId, start, now);

        Map<LocalDateTime, List<GateBeeCount>> grouped = beeCounts.stream()
                .collect(Collectors.groupingBy(b -> period.truncate(b.getRecordedAt())));

        List<DataPoint> data = generateSlots(start, now, period).stream()
                .map(slot -> DataPoint.of(
                        period.formatLabel(slot),
                        calculateAverage(slot, grouped, GateBeeCount::getEntranceIn),
                        calculateAverage(slot, grouped, GateBeeCount::getEntranceOut),
                        calculateAverage(slot, grouped, GateBeeCount::getExitIn),
                        calculateAverage(slot, grouped, GateBeeCount::getExitOut)
                ))
                .toList();

        return GateBeeCountResponse.of(period, data);
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

    private Integer calculateAverage(LocalDateTime slot, Map<LocalDateTime, List<GateBeeCount>> grouped,
                                     ToIntFunction<GateBeeCount> extractor) {
        List<GateBeeCount> items = grouped.getOrDefault(slot, List.of());

        return items.isEmpty() ? null
                : (int) Math.round(items.stream().mapToInt(extractor).average().orElse(0.0));
    }
}
