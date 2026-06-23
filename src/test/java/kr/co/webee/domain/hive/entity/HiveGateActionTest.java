package kr.co.webee.domain.hive.entity;

import kr.co.webee.domain.hive.type.GateActionType;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HiveGateActionTest {

    @Nested
    @DisplayName("개폐기 동작 생성")
    class Create {

        @Test
        @DisplayName("정상적인 값으로 개폐기 동작을 생성한다.")
        void createHiveGateAction() {
            //given
            Hive hive = TestFixture.createHive(null, TestFixture.createUser(null));

            //when
            HiveGateAction action = HiveGateAction.builder()
                    .hive(hive)
                    .title("새벽 환기")
                    .actionType(GateActionType.OPEN_ONLY)
                    .actionTime(LocalTime.of(9, 0))
                    .repeatEnabled(true)
                    .build();

            //then
            assertThat(action.getTitle()).isEqualTo("새벽 환기");
            assertThat(action.getActionType()).isEqualTo(GateActionType.OPEN_ONLY);
            assertThat(action.getActionTime()).isEqualTo(LocalTime.of(9, 0));
            assertThat(action.isRepeatEnabled()).isTrue();
        }

        @Test
        @DisplayName("제목이 빈 문자열인 경우 예외가 발생한다.")
        void createWithBlankTitle() {
            //given
            Hive hive = TestFixture.createHive(null, TestFixture.createUser(null));

            //when - then
            assertThatThrownBy(() -> HiveGateAction.builder()
                    .hive(hive)
                    .title("")
                    .actionType(GateActionType.OPEN_ONLY)
                    .actionTime(LocalTime.of(9, 0))
                    .repeatEnabled(false)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("hive가 null인 경우 예외가 발생한다.")
        void createWithNullHive() {
            //when - then
            assertThatThrownBy(() -> HiveGateAction.builder()
                    .hive(null)
                    .title("새벽 환기")
                    .actionType(GateActionType.OPEN_ONLY)
                    .actionTime(LocalTime.of(9, 0))
                    .repeatEnabled(false)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
