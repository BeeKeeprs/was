package kr.co.webee.application.interestpesticide.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.interestpesticide.repository.InterestPesticideRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.interestpesticide.dto.request.InterestPesticideRegisterRequest;
import kr.co.webee.presentation.interestpesticide.dto.response.InterestPesticideRegisterResponse;
import kr.co.webee.presentation.interestpesticide.dto.response.InterestPesticideListResponse;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class InterestPesticideServiceTest {

    @Autowired
    private InterestPesticideService interestPesticideService;

    @Autowired
    private InterestPesticideRepository interestPesticideRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        interestPesticideRepository.deleteAllInBatch();
        user = userRepository.save(TestFixture.createUser("pesticide-user"));
    }

    @Nested
    @DisplayName("관심 농약 등록")
    class RegisterInterestPesticide {

        @Test
        @DisplayName("관심 농약을 등록한다.")
        void registerInterestPesticide() {
            //given
            InterestPesticideRegisterRequest request = InterestPesticideRegisterRequest.builder()
                    .pesticideApplicationNo("1-1-000001")
                    .brandName("유기농바이오킬")
                    .productName("비티수화제")
                    .cropName("벼")
                    .build();

            //when
            InterestPesticideRegisterResponse response = interestPesticideService.registerInterestPesticide(request, user.getId());

            //then
            assertThat(response.interestPesticideId()).isNotNull();
            assertThat(interestPesticideRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("이미 등록된 농약을 다시 등록하면 예외가 발생한다.")
        void registerInterestPesticide_duplicate() {
            //given
            interestPesticideService.registerInterestPesticide(
                    InterestPesticideRegisterRequest.builder()
                            .pesticideApplicationNo("1-1-000001")
                            .build(),
                    user.getId());

            //when - then
            assertThatThrownBy(() -> interestPesticideService.registerInterestPesticide(
                    InterestPesticideRegisterRequest.builder()
                            .pesticideApplicationNo("1-1-000001")
                            .build(),
                    user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.INTEREST_PESTICIDE_ALREADY_EXISTS);
        }

        @Test
        @DisplayName("존재하지 않는 사용자로 등록하면 예외가 발생한다.")
        void registerInterestPesticide_userNotFound() {
            //when - then
            assertThatThrownBy(() -> interestPesticideService.registerInterestPesticide(
                    InterestPesticideRegisterRequest.builder()
                            .pesticideApplicationNo("1-1-000001")
                            .build(),
                    -1L))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("관심 농약 목록 조회")
    class GetAllInterestPesticides {

        @Test
        @DisplayName("등록한 관심 농약 목록을 조회한다.")
        void getAllInterestPesticides() {
            //given
            interestPesticideService.registerInterestPesticide(
                    InterestPesticideRegisterRequest.builder()
                            .pesticideApplicationNo("1-1-000001")
                            .cropName("딸기")
                            .build(),
                    user.getId());
            interestPesticideService.registerInterestPesticide(
                    InterestPesticideRegisterRequest.builder()
                            .pesticideApplicationNo("1-1-000002")
                            .cropName("벼")
                            .build(),
                    user.getId());

            //when
            Slice<InterestPesticideListResponse> result = interestPesticideService.getAllInterestPesticides(
                    user.getId(), PageRequest.of(0, 10));

            //then
            assertThat(result.getContent()).hasSize(2)
                    .extracting("pesticideApplicationNo")
                    .containsExactlyInAnyOrder("1-1-000001", "1-1-000002");
        }

        @Test
        @DisplayName("등록한 관심 농약이 없으면 빈 목록을 반환한다.")
        void getAllInterestPesticides_empty() {
            //when
            Slice<InterestPesticideListResponse> result = interestPesticideService.getAllInterestPesticides(
                    user.getId(), PageRequest.of(0, 10));

            //then
            assertThat(result.getContent()).isEmpty();
        }

        @Test
        @DisplayName("페이지 크기보다 많으면 hasNext가 true다.")
        void getAllInterestPesticides_hasNext() {
            //given
            interestPesticideService.registerInterestPesticide(
                    InterestPesticideRegisterRequest.builder()
                            .pesticideApplicationNo("1-1-000001")
                            .build(),
                    user.getId());
            interestPesticideService.registerInterestPesticide(
                    InterestPesticideRegisterRequest.builder()
                            .pesticideApplicationNo("1-1-000002")
                            .build(),
                    user.getId());

            //when
            Slice<InterestPesticideListResponse> result = interestPesticideService.getAllInterestPesticides(
                    user.getId(), PageRequest.of(0, 1));

            //then
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getContent()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("관심 농약 삭제")
    class RemoveInterestPesticide {

        @Test
        @DisplayName("관심 농약을 삭제한다.")
        void removeInterestPesticide() {
            //given
            Long id = interestPesticideService.registerInterestPesticide(
                    InterestPesticideRegisterRequest.builder()
                            .pesticideApplicationNo("1-1-000001")
                            .build(),
                    user.getId()).interestPesticideId();

            //when
            interestPesticideService.removeInterestPesticide(id, user.getId());

            //then
            assertThat(interestPesticideRepository.count()).isZero();
        }

        @Test
        @DisplayName("존재하지 않는 관심 농약을 삭제하면 예외가 발생한다.")
        void removeInterestPesticide_notFound() {
            //when - then
            assertThatThrownBy(() -> interestPesticideService.removeInterestPesticide(-1L, user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.INTEREST_PESTICIDE_NOT_FOUND);
        }

        @Test
        @DisplayName("다른 사용자의 관심 농약을 삭제하면 예외가 발생한다.")
        void removeInterestPesticide_otherUser() {
            //given
            Long id = interestPesticideService.registerInterestPesticide(
                    InterestPesticideRegisterRequest.builder()
                            .pesticideApplicationNo("1-1-000001")
                            .build(),
                    user.getId()).interestPesticideId();
            User otherUser = userRepository.save(TestFixture.createUser("other-user"));

            //when - then
            assertThatThrownBy(() -> interestPesticideService.removeInterestPesticide(id, otherUser.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.INTEREST_PESTICIDE_NOT_FOUND);
        }
    }
}
