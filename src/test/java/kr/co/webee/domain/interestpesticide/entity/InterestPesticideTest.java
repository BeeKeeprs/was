package kr.co.webee.domain.interestpesticide.entity;

import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InterestPesticideTest {

    @Test
    @DisplayName("관심 농약을 생성한다.")
    void createInterestPesticide() {
        //when
        InterestPesticide interestPesticide = InterestPesticide.builder()
                .user(TestFixture.createUser(null))
                .pesticideApplicationNo("1-1-000001")
                .brandName("유기농바이오킬")
                .build();

        //then
        assertThat(interestPesticide.getPesticideApplicationNo()).isEqualTo("1-1-000001");
        assertThat(interestPesticide.getBrandName()).isEqualTo("유기농바이오킬");
    }

    @Test
    @DisplayName("pesticideApplicationNo가 빈 값이면 예외가 발생한다.")
    void createInterestPesticide_blankApplicationNo() {
        //when - then
        assertThatThrownBy(() -> InterestPesticide.builder()
                .user(TestFixture.createUser(null))
                .pesticideApplicationNo("")
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
