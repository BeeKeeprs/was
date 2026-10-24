package kr.co.webee.application.feedback.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.domain.feedback.type.InterestProduct;
import kr.co.webee.infrastructure.mail.MailService;
import kr.co.webee.presentation.feedback.dto.request.FeedbackSubmitRequest;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.verify;

@IntegrationTest
class FeedbackServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @MockitoBean
    private MailService mailService;

    @Nested
    @DisplayName("문의 메일 구성")
    class SubmitFeedback {
        @Test
        @DisplayName("입력한 문의 정보를 제목과 본문으로 구성해 전달한다.")
        void submitsAllFields() {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest();
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
            feedbackService.submitFeedback(request);

            //then
            verify(mailService).sendEmail("webee 사용자 피드백", content);
        }

        @Test
        @DisplayName("선택 정보가 없으면 본문에 미입력으로 표시한다.")
        void submitsWithoutOptionalFields() {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest(
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
            feedbackService.submitFeedback(request);

            //then
            verify(mailService).sendEmail("webee 사용자 피드백", content);
        }

        @Test
        @DisplayName("빈 선택 정보는 본문에 미입력으로 표시한다.")
        void submitsWithBlankOptionalFields() {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest(
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
            feedbackService.submitFeedback(request);

            //then
            verify(mailService).sendEmail("webee 사용자 피드백", content);
        }
    }

}
