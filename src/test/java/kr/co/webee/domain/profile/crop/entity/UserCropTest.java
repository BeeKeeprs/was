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
        User user = createUser();
        Location location = createLocation(12.34, 45.67, "충청남도 논산시 연무읍 봉동리");
        UserCrop userCrop = createUserCrop(location, user);
        Location modifiedLocation = createLocation(987.98, 123.12, "경상북도 경산시 대학로280");

        //when
        userCrop.updateCultivationLocation(modifiedLocation);

        //then
        assertThat(userCrop.getCultivationAddress()).isEqualTo(modifiedLocation.getAddress());
        assertThat(userCrop.getCultivationLocation().getCoordinates()).isEqualTo(modifiedLocation.getCoordinates());
    }

    @DisplayName("주어진 주소가 사용자 작물의 재배 주소와 같은지 확인한다.")
    @Test
    void isSameCultivationAddress(){
        //given
        User user = createUser();
        Location location = createLocation(12.34, 45.67, "충청남도 논산시 연무읍 봉동리");
        UserCrop userCrop = createUserCrop(location, user);

        String address="충청남도 논산시 연무읍 봉동리";

        //when
        boolean result = userCrop.isSameCultivationAddress(address);

        //then
        assertThat(result).isTrue();
    }

    private static Location createLocation(double longitude, double latitude, String address) {
        Coordinates coordinates = Coordinates.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();

        return Location.builder()
                .address(address)
                .coordinates(coordinates)
                .build();
    }

    private static UserCrop createUserCrop(Location location, User user) {
        return UserCrop.builder()
                .name("딸기")
                .variety("설향")
                .cultivationType(CultivationType.CONTROLLED)
                .cultivationLocation(location)
                .cultivationArea(1980)
                .plantingDate(LocalDate.of(2025, 3, 25))
                .user(user)
                .build();
    }

    private static User createUser() {
        return User.builder()
                .username("exampleUsername")
                .password("examplePassword")
                .name("exampleName")
                .build();
    }
}
