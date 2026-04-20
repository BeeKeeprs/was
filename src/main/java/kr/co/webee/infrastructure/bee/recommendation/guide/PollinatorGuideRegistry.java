package kr.co.webee.infrastructure.bee.recommendation.guide;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import kr.co.webee.infrastructure.bee.recommendation.guide.dto.PollinatorGuideCropCategoryDto;
import kr.co.webee.infrastructure.bee.recommendation.guide.dto.PollinatorGuideDto;
import kr.co.webee.infrastructure.bee.recommendation.guide.dto.PollinatorGuideDto.Crop;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Getter
@Slf4j
@Component
@RequiredArgsConstructor
public class PollinatorGuideRegistry {
    private final ObjectMapper objectMapper;
    private PollinatorGuideDto pollinatorGuide;
    private List<PollinatorGuideCropCategoryDto> cropCategories;

    @PostConstruct
    public void load() {
        try (InputStream inputStream = new ClassPathResource("pollinator-guide.json").getInputStream()) {
            this.pollinatorGuide = objectMapper.readValue(inputStream, PollinatorGuideDto.class);
            this.cropCategories = Collections.unmodifiableList(groupCropsByCategory(pollinatorGuide.crops()));
        } catch (IOException e) {
            log.error("Failed to load pollinator-guide.json", e);
            throw new IllegalStateException("Failed to load pollinator guide data", e);
        }
    }

    private static List<PollinatorGuideCropCategoryDto> groupCropsByCategory(List<Crop> crops) {
        Map<String, List<String>> map = new LinkedHashMap<>();

        for (Crop crop : crops)
            map.computeIfAbsent(crop.category(), k -> new ArrayList<>())
                    .add(crop.name());

        return map.entrySet().stream()
                .map(entry -> PollinatorGuideCropCategoryDto.of(
                        entry.getKey(),
                        entry.getValue().stream().toList()
                ))
                .toList();
    }
}
