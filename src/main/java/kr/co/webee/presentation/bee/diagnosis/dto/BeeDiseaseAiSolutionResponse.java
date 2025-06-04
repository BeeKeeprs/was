package kr.co.webee.presentation.bee.diagnosis.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "꿀벌 이미지 질병 판단 결과와 사용자 재배 정보를 바탕으로 한 맞춤형 ai 대처 방안 응답")
public record BeeDiseaseAiSolutionResponse(
    @Schema(
            description = "상황 분석",
            example = "[\"성충 날개불구바이러스감염증으로 인해 벌군의 건강이 악화됨.\", " +
                    "\"충청남도 논산시의 고온 현상이 벌의 활동에 영향을 미침.\", " +
                    "\"딸기 재배를 위한 수분이 필요하지만 벌군의 문제로 수확량 감소 위험이 있음.\"]")
    List<String> situationAnalysis,

    @Schema(
            description = "대처 방안",
            examples =  "[\"병든 벌을 즉시 제거하여 건강한 벌들만 남기는 관리 필요.\", " +
                    "\"온실 내부의 온도를 적절히 조절하여 벌의 활동을 촉진함.\", " +
                    "\"딸기 개화 시기에 맞춰 벌통 설치를 조정하여 수분 효율을 높임.\"]")
    List<String> solutions
) {
}
