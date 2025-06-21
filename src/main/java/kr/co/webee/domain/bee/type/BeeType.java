package kr.co.webee.domain.bee.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;


@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BeeType {
    HONEYBEE(
            "꿀벌",
            List.of(
                    "사회성 곤충 중에서 가장 진화한 곤충",
                    "여왕벌, 수벌, 일벌의 3계급으로 나뉘며, 일벌만이 수분 활동을 함.",
                    "시설 내에서 발육하여 나온 어린 벌들은 야외 방화활동을 하던 늙은 일벌들보다 방화활동을 활발"
            )
    ),

    BUMBLEBEE(
            "뒤영벌",
            List.of(
                    "크기가 크고 활동적이며, 꽃가루 수정 능력 탁월",
                    "흐리거나 추운 날씨, 온실과 같이 좁은 공간에서도 활동이 활발",
                    "꿀이 나지 않고 진동에 의해서 수정되는 토마토나 가지 등, 꿀벌로는 수정시키기 어려운 가지과 식물에서의 수정능력 우수",
                    "뒤영벌에 의해 수정된 과일은 열매가 크고(생산성 향상), 당도와 신도가 증가하여 맛이 좋아 품질향상에 도움을 주므로 농가 수익에 기여"
            )
    ),

    MASON_BEE(
            "머리뿔가위벌",
            List.of("단독성 화분매개곤충으로 광범위한 목초재배지나 과수원에서의 중요한 화분매개곤충",
                    "꿀벌의 단점을 보완한 대체 종",
                    "집단이 파괴되지 않기 때문에 필요한 양의 종 봉을 한 번만 입수하면 이용 후에도 농가 주변이나 산간지에서 자가 증식하여 재활용 가능하여 경제적"
            )
    );

    private final String name;
    private final List<String> characteristic;

    public static BeeType convertFrom(String type) {
        return Arrays.stream(BeeType.values())
                .filter(beeType -> beeType.name.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 수정벌 타입이 없습니다."));
    }

    public String description() {
        return String.format("종류: %s, 특징: %s", this.name, this.characteristic);
    }
}
