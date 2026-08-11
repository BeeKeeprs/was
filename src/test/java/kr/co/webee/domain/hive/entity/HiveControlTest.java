package kr.co.webee.domain.hive.entity;

import kr.co.webee.domain.hive.type.ControlType;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HiveControlTest {

    private Hive hive;

    @BeforeEach
    void setUp() {
        User user = TestFixture.createUser(null);
        hive = TestFixture.createHive(null, user);
    }

    @Nested
    @DisplayName("벌통 제어 설정 생성")
    class Create {

        @Test
        @DisplayName("벌통 제어 설정을 생성한다.")
        void create() {
            //when
            HiveControl control = HiveControl.create(hive, ControlType.TEMPERATURE);

            //then
            assertThat(control.getType()).isEqualTo(ControlType.TEMPERATURE);
            assertThat(control.getTargetValue()).isNull();
        }

        @Test
        @DisplayName("hive가 null이면 예외가 발생한다.")
        void createWithNullHive() {
            //when - then
            assertThatThrownBy(() -> HiveControl.create(null, ControlType.TEMPERATURE))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("type이 null이면 예외가 발생한다.")
        void createWithNullType() {
            //when - then
            assertThatThrownBy(() -> HiveControl.create(hive, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("목표값 업데이트")
    class UpdateTargetValue {

        @Test
        @DisplayName("목표값을 업데이트한다.")
        void updateTargetValue() {
            //given
            HiveControl control = HiveControl.create(hive, ControlType.TEMPERATURE);

            //when
            control.updateTargetValue(36.5);

            //then
            assertThat(control.getTargetValue()).isEqualTo(36.5);
        }

        @Test
        @DisplayName("목표값을 여러 번 업데이트하면 마지막 값이 반영된다.")
        void updateTargetValueMultipleTimes() {
            //given
            HiveControl control = HiveControl.create(hive, ControlType.HUMIDITY);
            control.updateTargetValue(50.0);

            //when
            control.updateTargetValue(65.0);

            //then
            assertThat(control.getTargetValue()).isEqualTo(65.0);
        }
    }
}
