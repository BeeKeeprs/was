package kr.co.webee.domain.bee.diagnosis.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DiseaseType {

    MITE("응애","꿀벌의 체액을 빨아먹으며 바이러스 감염도 유발함. 주로 애벌레나 번데기에 기생하며 군체를 약화시킴.","봉군 교체 및 번식기 관리로 감염 밀도 조절"),
    AFB("부저병","꿀벌 애벌레에 감염되어 죽게 만드는 전염성 세균성 질병. 감염된 애벌레는 끈적한 갈색으로 변하며 불쾌한 냄새가 남.","감염된 군체 및 장비 소각"),
    CHALKBROOD("석고병","애벌레가 곰팡이에 감염되어 하얗고 딱딱한 석고처럼 굳음.","벌통 통풍 개선 및 습도 조절"),
    DWV("날개불구바이러스감염증","주로 Varroa 응애를 통해 전파되며 꿀벌의 날개가 비정상적으로 짧거나 말려서 나타남.","감염된 벌 제거 및 군체 교체");

    private final String name;
    private final String description;
    private final String treatment;
}




