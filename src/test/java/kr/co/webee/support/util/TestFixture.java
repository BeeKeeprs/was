package kr.co.webee.support.util;

import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveGateAction;
import kr.co.webee.domain.hive.type.GateActionType;
import kr.co.webee.domain.user.entity.User;

import java.time.LocalTime;

public class TestFixture {

    public static User createUser(String username) {
        return User.builder()
                .username(username != null ? username : "test-user")
                .password("password")
                .name("테스트유저")
                .businessRegistered(false)
                .build();
    }

    public static Hive createHive(String macAddress, User user) {
        return Hive.builder()
                .macAddress(macAddress != null ? macAddress : "AA:BB:CC:DD:EE:FF")
                .name("테스트 벌통")
                .region("서울")
                .location("강남구")
                .user(user)
                .build();
    }

    public static HiveGateAction createHiveGateAction(String title, GateActionType actionType, LocalTime actionTime, Hive hive) {
        return HiveGateAction.builder()
                .hive(hive)
                .title(title != null ? title : "테스트 개폐기 동작")
                .actionType(actionType != null ? actionType : GateActionType.OPEN_ONLY)
                .actionTime(actionTime != null ? actionTime : LocalTime.of(9, 0))
                .repeatEnabled(false)
                .build();
    }
}
