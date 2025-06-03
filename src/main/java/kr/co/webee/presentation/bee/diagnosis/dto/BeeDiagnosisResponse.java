package kr.co.webee.presentation.bee.diagnosis.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.diagnosis.type.DiseaseType;

import java.util.List;

@Schema(description = "꿀벌 질병 진단 결과 DTO")
public record BeeDiagnosisResponse(
        @Schema(description = "꿀벌 질병 타입", example = "성충 날개불구바이러스감염증")
        String name,

        @Schema(description = "신뢰도", example = "78.60%")
        String confidence,

        @Schema(description = "설명", example = "날개불구바이러스(DWV)에 감염된 벌의 날개가 기형적으로 형성되어 비행이 어려운 질병입니다.")
        String description,

        @Schema(
                description = "증상",
                example = """
                        [
                            "날개가 기형적으로 접히거나 짧게 형성되어 정상적인 비행이 불가능함",
                            "벌이 꿀을 채집하거나 외부 활동을 하지 못하고 벌통 안에 머무는 경우가 많음",
                            "감염된 벌은 외형적으로 왜소하며 생존 기간이 일반 벌보다 현저히 짧음"
                        ]
                        """
        )
        List<String> symptoms,

        @Schema(description = "원인", example = "날개불구바이러스(DWV)에 의한 감염으로, 주로 응애를 통해 전파됨")
        String cause,

        @Schema(description = "심각도", example = "중 — 초기에 격리 및 봉군 관리로 군체 유지 가능")
        String severity
        ) {
    public static BeeDiagnosisResponse from(String type, Double confidence) {
        DiseaseType diseaseType = DiseaseType.convertFrom(type);

        return new BeeDiagnosisResponse(
                diseaseType.getName(),
                String.format("%.2f%%", confidence * 100),
                diseaseType.getDescription(),
                diseaseType.getSymptoms(),
                diseaseType.getCause(),
                diseaseType.getSeverity()
        );
    }
}
