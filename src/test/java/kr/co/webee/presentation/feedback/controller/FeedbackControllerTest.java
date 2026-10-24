package kr.co.webee.presentation.feedback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.webee.application.feedback.service.FeedbackService;
import kr.co.webee.common.auth.jwt.JwtProvider;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.feedback.type.InterestProduct;
import kr.co.webee.infrastructure.config.SecurityConfig;
import kr.co.webee.presentation.config.WebConfig;
import kr.co.webee.presentation.feedback.dto.request.FeedbackSubmitRequest;
import kr.co.webee.support.util.TestFixture;
import kr.co.webee.presentation.support.filter.JwtAuthenticationEntryPoint;
import kr.co.webee.presentation.support.filter.JwtAuthenticationFilter;
import kr.co.webee.presentation.support.filter.JwtExceptionFilter;
import kr.co.webee.presentation.support.resolver.UserIdArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = FeedbackController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserIdArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtExceptionFilter.class,
        JwtAuthenticationEntryPoint.class})
@ActiveProfiles("test")
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FeedbackService feedbackService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Nested
    @DisplayName("문의 등록")
    class SubmitFeedback {

        @Test
        @DisplayName("로그인 없이 문의를 등록하고 관심 제품을 enum으로 전달한다.")
        void submitFeedback() throws Exception {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest();

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."));

            verify(feedbackService).submitFeedback(request);
        }

        @Test
        @DisplayName("선택 정보가 null이어도 문의를 등록한다.")
        void submitFeedbackWithoutOptionalFields() throws Exception {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest(
                    "홍길동", null, null,
                    null, InterestProduct.SMART_HIVE, "도입 문의");

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(feedbackService).submitFeedback(request);
        }

        @Test
        @DisplayName("선택 정보를 생략해도 문의를 등록한다.")
        void submitFeedbackWithOmittedFields() throws Exception {
            //given
            String request = """
                    {
                      "name": "홍길동",
                      "interestProduct": "SMART_HIVE",
                      "message": "도입 문의"
                    }
                    """;

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(request)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("이름이 없으면 400을 반환한다.")
        void submitFeedbackWithoutName() throws Exception {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest(
                    null, "010-1234-5678", "user@example.com",
                    "위비", InterestProduct.SMART_HIVE, "도입 문의");

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorType.FAILED_VALIDATION.getCode()));

            verifyNoInteractions(feedbackService);
        }

        @Test
        @DisplayName("관심 제품이 없으면 400을 반환한다.")
        void submitFeedbackWithoutInterestProduct() throws Exception {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest(
                    "홍길동", "010-1234-5678", "user@example.com",
                    "위비", null, "도입 문의");

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorType.FAILED_VALIDATION.getCode()));

            verifyNoInteractions(feedbackService);
        }

        @Test
        @DisplayName("문의 내용이 없으면 400을 반환한다.")
        void submitFeedbackWithoutMessage() throws Exception {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest(
                    "홍길동", "010-1234-5678", "user@example.com",
                    "위비", InterestProduct.SMART_HIVE, null);

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorType.FAILED_VALIDATION.getCode()));

            verifyNoInteractions(feedbackService);
        }

        @Test
        @DisplayName("이름이 공백이면 400을 반환한다.")
        void submitFeedbackWithBlankName() throws Exception {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest(
                    " ", "010-1234-5678", "user@example.com",
                    "위비", InterestProduct.SMART_HIVE, "도입 문의");

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(feedbackService);
        }

        @Test
        @DisplayName("문의 내용이 공백이면 400을 반환한다.")
        void submitFeedbackWithBlankMessage() throws Exception {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest(
                    "홍길동", "010-1234-5678", "user@example.com",
                    "위비", InterestProduct.SMART_HIVE, " ");

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(feedbackService);
        }

        @Test
        @DisplayName("허용하지 않는 관심 제품이면 400을 반환한다.")
        void submitFeedbackWithInvalidProduct() throws Exception {
            //given
            String request = """
                    {
                      "name": "홍길동",
                      "interestProduct": "unknown",
                      "message": "도입 문의"
                    }
                    """;

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(request)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(feedbackService);
        }

        @Test
        @DisplayName("enum 상수명이 아닌 관심 제품이면 400을 반환한다.")
        void submitFeedbackWithProductValue() throws Exception {
            //given
            String request = """
                    {
                      "name": "홍길동",
                      "interestProduct": "smart-hive",
                      "message": "도입 문의"
                    }
                    """;

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(request)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(feedbackService);
        }

        @Test
        @DisplayName("관심 제품이 공백이면 400을 반환한다.")
        void submitFeedbackWithBlankProduct() throws Exception {
            //given
            String request = """
                    {
                      "name": "홍길동",
                      "interestProduct": " ",
                      "message": "도입 문의"
                    }
                    """;

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(request)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(feedbackService);
        }

        @Test
        @DisplayName("이메일 형식이 올바르지 않으면 400을 반환한다.")
        void submitFeedbackWithInvalidEmail() throws Exception {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest(
                    "홍길동", "010-1234-5678", "invalid-email",
                    "위비", InterestProduct.SMART_HIVE, "도입 문의");

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(feedbackService);
        }

        @Test
        @DisplayName("메일 전송 실패 시 500을 반환한다.")
        void submitFeedbackWithMailFailure() throws Exception {
            //given
            FeedbackSubmitRequest request = TestFixture.createFeedbackSubmitRequest();
            doThrow(new BusinessException(ErrorType.UNHANDLED_EXCEPTION))
                    .when(feedbackService).submitFeedback(any(FeedbackSubmitRequest.class));

            //when - then
            mockMvc.perform(post("/api/v1/suggestions")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Test
    @DisplayName("다른 비공개 경로는 로그인이 필요하다.")
    void requestPrivateEndpointWithoutLogin() throws Exception {
        //when - then
        mockMvc.perform(post("/api/v1/private"))
                .andExpect(status().isUnauthorized());
    }

}
