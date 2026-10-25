package kr.co.webee.application.inquiry.service;

import kr.co.webee.annotation.IntegrationTest;
import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.application.feedback.service.FeedbackService;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.feedback.dto.FeedbackSubmitRequest;
import kr.co.webee.domain.inquiry.type.InterestProduct;
import kr.co.webee.infrastructure.mail.MailService;
import kr.co.webee.presentation.inquiry.dto.request.InquirySubmitRequest;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class InquiryServiceTest {

    @Autowired
    private InquiryService inquiryService;

    @MockitoBean
    private MailService mailService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("문의 메일 구성")
    class SubmitInquiry {
        @Test
        @DisplayName("입력한 문의 정보를 제목과 본문으로 구성해 전달한다.")
        void submitsAllFields() {
            //given
            InquirySubmitRequest request = TestFixture.createInquirySubmitRequest();
            String content = """
                    이름: 홍길동
                    전화번호: 010-1234-5678
                    이메일: user@example.com
                    소속: 위비
                    관심 제품: smart-hive

                    문의 내용:
                    도입 문의
                    """;

            //when
            inquiryService.submitInquiry(request);

            //then
            verify(mailService).sendEmail("webee 사용자 문의", content);
        }

        @Test
        @DisplayName("선택 정보가 없으면 본문에 미입력으로 표시한다.")
        void submitsWithoutOptionalFields() {
            //given
            InquirySubmitRequest request = TestFixture.createInquirySubmitRequest(
                    "홍길동", null, null, null, InterestProduct.SMART_HIVE, "도입 문의");
            String content = """
                    이름: 홍길동
                    전화번호: 미입력
                    이메일: 미입력
                    소속: 미입력
                    관심 제품: smart-hive

                    문의 내용:
                    도입 문의
                    """;

            //when
            inquiryService.submitInquiry(request);

            //then
            verify(mailService).sendEmail("webee 사용자 문의", content);
        }

        @Test
        @DisplayName("빈 선택 정보는 본문에 미입력으로 표시한다.")
        void submitsWithBlankOptionalFields() {
            //given
            InquirySubmitRequest request = TestFixture.createInquirySubmitRequest(
                    "홍길동", " ", "", " ", InterestProduct.SMART_HIVE, "도입 문의");
            String content = """
                    이름: 홍길동
                    전화번호: 미입력
                    이메일: 미입력
                    소속: 미입력
                    관심 제품: smart-hive

                    문의 내용:
                    도입 문의
                    """;

            //when
            inquiryService.submitInquiry(request);

            //then
            verify(mailService).sendEmail("webee 사용자 문의", content);
        }
    }

    @Nested
    @DisplayName("기존 피드백 메일 전송")
    class SubmitFeedback {

        @Test
        @DisplayName("기존 피드백은 로그인 사용자 이름과 이메일 및 내용으로 전송한다.")
        void submitFeedback() {
            //given
            User user = userRepository.save(TestFixture.createUser("feedback-regression-user"));
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest();

            //when
            feedbackService.submitFeedback(request, user.getId());

            //then
            verify(mailService).sendEmail(request.content(), user.getName(), request.email());
        }

        @Test
        @DisplayName("기존 피드백에서 사용자가 없으면 예외가 발생한다.")
        void submitFeedbackWithoutUser() {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest();

            //when - then
            assertThatThrownBy(() -> feedbackService.submitFeedback(request, Long.MAX_VALUE))
                    .isInstanceOf(EntityNotFoundException.class);
            verifyNoInteractions(mailService);
        }
    }
}
