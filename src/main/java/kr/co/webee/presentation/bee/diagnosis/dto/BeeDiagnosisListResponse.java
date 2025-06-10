package kr.co.webee.presentation.bee.diagnosis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.diagnosis.entity.BeeDiagnosis;
import kr.co.webee.domain.bee.diagnosis.type.DiseaseType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BeeDiagnosisListResponse(
        @Schema(description = "사업자등록증 이미지 url", example = "https://webee.net//bee-diagnosis1/성충 날개불구바이러스감염증.jpeg")
        String imageUrl,

        @Schema(description = "꿀벌 질병 타입", example = "성충 날개불구바이러스감염증")
        DiseaseType diseaseType,

        @Schema(
                description = "상황 분석",
                example = "성충 날개불구바이러스감염증으로 인해 벌군의 건강이 악화됨.\n" +
                        "충청남도 논산시의 고온 현상이 벌의 활동에 영향을 미침.\n"+
                        "딸기 재배를 위한 수분이 필요하지만 벌군의 문제로 수확량 감소 위험이 있음.")
        String situationAnalysis,

        @Schema(description = "진단 날짜", example = "2025-04-01")
        LocalDate createdAt
) {
        public static BeeDiagnosisListResponse from(BeeDiagnosis beeDiagnosis) {
                return BeeDiagnosisListResponse.builder()
                        .imageUrl(beeDiagnosis.getImageUrl())
                        .diseaseType(beeDiagnosis.getDiseaseType())
                        .situationAnalysis(beeDiagnosis.getSituationAnalysis())
                        .createdAt(LocalDate.from(beeDiagnosis.getCreatedAt()))
                        .build();
        }
}
