package kr.co.webee.domain.bee.diagnosis.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DiseaseType {
    ADULT_WING_DEFORMITY_VIRUS(
            "성충 날개불구바이러스감염증",
            "날개불구바이러스(DWV)에 감염된 벌의 날개가 기형적으로 형성되어 비행이 어려운 질병입니다.",
            List.of(
                    "날개가 기형적으로 접히거나 짧게 형성되어 정상적인 비행이 불가능함",
                    "벌이 꿀을 채집하거나 외부 활동을 하지 못하고 벌통 안에 머무는 경우가 많음",
                    "감염된 벌은 외형적으로 왜소하며 생존 기간이 일반 벌보다 현저히 짧음"
            ),
            "날개불구바이러스(DWV)에 의한 감염으로, 주로 응애를 통해 전파됨",
            "중 — 초기에 격리 및 봉군 관리로 군체 유지 가능"
    ),

    ADULT_VARROA_MITE(
            "성충 응애",
            "Varroa 응애가 꿀벌 성충에 기생하여 체액을 빨아먹고 면역력을 약화시키는 대표적 외부 기생충 질병입니다.",
            List.of(
                    "응애가 꿀벌의 체액을 지속적으로 흡수하여 벌이 점점 쇠약해지고 무기력해짐",
                    "기생 부위에 검은 반점이나 탈피 껍질 같은 흔적이 관찰되며 외형 이상이 나타남",
                    "감염이 지속되면 벌들이 의욕을 잃고 채집 활동이 급격히 저하됨"
            ),
            "Varroa destructor라는 응애가 꿀벌 성충에 기생하면서 발생",
            "중 — 주기적인 방제가 없으면 군체 붕괴 위험이 큼"
    ),

    ADULT_NORMAL(
            "성충 정상",
            "질병이나 이상이 없는 건강한 성충 상태입니다.",
            List.of(),
            "없음",
            "없음"
    ),

    LARVA_FOULBROOD(
            "유충 부저병",
            "박테리아에 감염된 유충이 썩으며 악취를 동반하는 전염성 질병입니다.",
            List.of(
                    "감염된 유충이 갈색 액체로 변하며 끈적이는 점액질 상태로 부패됨",
                    "셀 덮개가 함몰되며 내부에서 특유의 심한 악취가 발생함",
                    "죽은 유충이 썩은 상태로 셀 안에 남아 주변 유충에게 빠르게 전염됨"
            ),
            "Paenibacillus larvae라는 박테리아에 감염되어 발생",
            "높음 — 전염성이 강해 전체 군체로 빠르게 확산될 수 있음"
    ),

    LARVA_SACBROOD(
            "유충 석고병",
            "석고처럼 굳은 유충 사체가 특징인 바이러스성 질병입니다.",
            List.of(
                    "감염된 유충이 자라지 못하고 피부가 말라붙어 단단히 굳는 현상이 나타남",
                    "유충의 몸이 희게 변색되어 석고처럼 보이며 죽은 상태로 고정됨",
                    "벌이 유충을 제거하지 못해 셀 안에 말라붙은 사체가 장기간 남아 있음"
            ),
            "Sacbrood virus(SBV)에 의한 감염으로, 주로 감염된 꿀벌이나 기구를 통해 전파됨",
            "중 — 조기 발견 시 봉군 관리로 확산 억제 가능"
    ),

    LARVA_VARROA_MITE(
            "유충 응애",
            "Varroa 응애가 유충에 기생해 체액을 흡수하고 성장 저해 및 2차 감염을 유발하는 질병입니다.",
            List.of(
                    "응애가 유충의 체액을 지속적으로 흡수해 성장에 심각한 지장을 초래함",
                    "기생된 유충은 정상적인 번데기 성장 단계에 도달하지 못하고 기형이 발생함",
                    "면역력이 크게 약화되어 2차 감염에도 쉽게 노출됨"
            ),
            "Varroa destructor 응애가 유충에 직접 기생하면서 발생",
            "중 — 응애 방제를 소홀히 하면 전체 군체 약화로 이어질 수 있음"
    ),

    LARVA_NORMAL(
            "유충 정상",
            "질병이나 이상 증상이 없는 건강한 유충 상태입니다.",
            List.of(),
            "없음",
            "없음"
    );

    private final String name;
    private final String description;
    private final List<String> symptoms;
    private final String cause;
    private final String severity;

    public static DiseaseType convertFrom(String name) {
        return Arrays.stream(DiseaseType.values())
                .filter(disease -> disease.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 질병 타입이 없습니다."));
    }
}
