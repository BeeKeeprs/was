package kr.co.webee.domain.profile.crop.entity;

import kr.co.webee.domain.profile.crop.type.CultivationType;
import kr.co.webee.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserCropTest {

    @DisplayName("사용자 작물의 재배 지역을 수정할 수 있다.")
    @Test
    void updateCultivationLocation(){
        //given
        User user = User.builder()
                .username("exampleUsername")
                .password("examplePassword")
                .name("exampleName")
                .build();

        Coordinates coordinates = Coordinates.builder()
                .longitude(12.34)
                .latitude(45.67)
                .build();

        Location location=Location.builder()
                .address("충청남도 논산시 연무읍 봉동리")
                .coordinates(coordinates)
                .build();

        UserCrop userCrop = UserCrop.builder()
                .name("딸기")
                .variety("설향")
                .cultivationType(CultivationType.CONTROLLED)
                .cultivationLocation(location)
                .cultivationArea(1980)
                .plantingDate(LocalDate.of(2025, 3, 25))
                .user(user)
                .build();

        Coordinates modifiedCoordinates = Coordinates.builder()
                .longitude(987.98)
                .latitude(123.12)
                .build();

        Location modifiedLocation=Location.builder()
                .address("경상북도 경산시 대학로280")
                .coordinates(modifiedCoordinates)
                .build();

        //when
        userCrop.updateCultivationLocation(modifiedLocation);

        //then
        assertThat(userCrop.getCultivationAddress()).isEqualTo(modifiedLocation.getAddress());
        assertThat(userCrop.getCultivationLocation().getCoordinates()).isEqualTo(modifiedLocation.getCoordinates());
    }
}

/*
* String name, String variety, CultivationType cultivationType,
                    Location cultivationLocation, Integer cultivationArea, LocalDate plantingDate, User user
* */
