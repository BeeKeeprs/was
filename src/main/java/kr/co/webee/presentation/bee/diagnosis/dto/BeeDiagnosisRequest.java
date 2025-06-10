package kr.co.webee.presentation.bee.diagnosis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.bee.diagnosis.entity.BeeDiagnosis;
import kr.co.webee.domain.bee.diagnosis.type.DiseaseType;
import kr.co.webee.domain.profile.crop.type.CultivationType;
import kr.co.webee.domain.user.entity.User;


@Schema(description = "꿀벌 질병 진단 결과 저장 request")
public record BeeDiagnosisRequest(
        @Schema(description = "꿀벌 질병 타입", example = "성충 날개불구바이러스감염증")
        @NotBlank
        String disease,

        @Schema(description = "신뢰도", example = "93.21")
        @NotNull
        Double confidence,

        @Schema(description = "재배 방식", example = "CONTROLLED", examples = {"CONTROLLED", "OPEN_FIELD"})
        @NotNull
        CultivationType cultivationType,

        @Schema(description = "작물명", example = "딸기")
        @NotBlank
        String cropName,

        @Schema(description = "재배 지역", example = "충청남도 논산시 연무읍 봉동리")
        @NotBlank
        String cultivationAddress,

        @Schema(description = "추가 정보", example = "특이사항 또는 주변 환경 등")
        @NotBlank
        String details,

        @Schema(
                description = "상황 분석",
                example = "성충 날개불구바이러스감염증으로 인해 벌군의 건강이 악화됨.\n" +
                        "충청남도 논산시의 고온 현상이 벌의 활동에 영향을 미침.\n"+
                        "딸기 재배를 위한 수분이 필요하지만 벌군의 문제로 수확량 감소 위험이 있음.")
        @NotBlank
        String situationAnalysis,

        @Schema(
                description = "대처 방안",
                examples =  "병든 벌을 즉시 제거하여 건강한 벌들만 남기는 관리 필요.\n"+
                        "온실 내부의 온도를 적절히 조절하여 벌의 활동을 촉진함.\n" +
                        "딸기 개화 시기에 맞춰 벌통 설치를 조정하여 수분 효율을 높임.")
        @NotBlank
        String solutions
) {
        public BeeDiagnosis toEntity(String imageUrl, User user) {
                return BeeDiagnosis.builder()
                        .imageUrl(imageUrl)
                        .diseaseType(DiseaseType.convertFrom(disease))
                        .confidence(confidence)
                        .cropName(cropName)
                        .cultivationType(cultivationType)
                        .cultivationAddress(cultivationAddress)
                        .details(details)
                        .situationAnalysis(situationAnalysis)
                        .solutions(solutions)
                        .user(user)
                        .build();
        }
}
