package kr.co.webee.support.util;

import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveBeeCount;
import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.entity.HiveControlSchedule;
import kr.co.webee.domain.hive.entity.HiveGateAction;
import kr.co.webee.domain.hive.entity.HiveReplacementHistory;
import kr.co.webee.domain.hive.entity.HiveTelemetry;
import kr.co.webee.domain.hive.type.ControlType;
import kr.co.webee.domain.hive.type.GateActionType;
import kr.co.webee.domain.news.entity.NewsArticle;
import kr.co.webee.domain.news.entity.NewsArticleKeyword;
import kr.co.webee.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static HiveBeeCount createHiveBeeCount(Hive hive) {
        return HiveBeeCount.builder()
                .hive(hive)
                .count(100)
                .recordedAt(LocalDateTime.now())
                .build();
    }

    public static HiveControl createHiveControl(ControlType type, Hive hive) {
        return HiveControl.builder()
                .hive(hive)
                .type(type != null ? type : ControlType.FAN)
                .autoEnabled(false)
                .manualEnabled(false)
                .isOn(false)
                .build();
    }

    public static HiveControlSchedule createHiveControlSchedule(Hive hive) {
        return HiveControlSchedule.builder()
                .hive(hive)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(18, 0))
                .enabled(true)
                .build();
    }

    public static HiveReplacementHistory createHiveReplacementHistory(Hive hive) {
        return HiveReplacementHistory.builder()
                .hive(hive)
                .replacedAt(LocalDate.now())
                .build();
    }

    public static HiveTelemetry createHiveTelemetry(Hive hive) {
        return HiveTelemetry.builder()
                .hive(hive)
                .internalTemperature(25.0)
                .recordedAt(LocalDateTime.now())
                .build();
    }

    public static NewsArticle createNewsArticle(String title, String originalLink) {
        return NewsArticle.builder()
                .title(title != null ? title : "테스트 뉴스 기사")
                .summary("테스트 요약")
                .originalLink(originalLink != null ? originalLink : "https://news.example.com/test")
                .source("테스트 언론사")
                .publishedAt(LocalDateTime.of(2026, 6, 16, 9, 0))
                .fetchedAt(LocalDateTime.now())
                .build();
    }

    public static NewsArticleKeyword createNewsArticleKeyword(NewsArticle article, String keyword) {
        return NewsArticleKeyword.builder()
                .newsArticle(article)
                .keyword(keyword != null ? keyword : "꿀벌")
                .build();
    }
}
