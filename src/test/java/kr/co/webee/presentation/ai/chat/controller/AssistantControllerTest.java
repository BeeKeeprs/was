package kr.co.webee.presentation.ai.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.webee.application.ai.AssistantService;
import kr.co.webee.config.TestWebConfig;
import kr.co.webee.presentation.ai.chat.dto.AssistantRequest;
import kr.co.webee.presentation.ai.chat.dto.AssistantResponse;
import kr.co.webee.presentation.config.WebConfig;
import kr.co.webee.presentation.support.resolver.UserIdArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestWebConfig.class)
@WebMvcTest(
        controllers = AssistantController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserIdArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@ActiveProfiles("test")
class AssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AssistantService assistantService;

    @Nested
    @DisplayName("AI 질문 응답 생성")
    class AnswerUserInput {

        @Test
        @DisplayName("질문에 대한 AI 답변과 출처를 반환한다.")
        void answerUserInput() throws Exception {
            //given
            AssistantRequest request = new AssistantRequest("호박벌은 어디에 쓰이죠?", "conv-id", null);
            AssistantResponse response = new AssistantResponse(
                    "호박벌은 온실 작물의 수정에 사용됩니다.",
                    "conv-id",
                    List.of("농촌진흥청 양봉 매뉴얼", "국립농업과학원 수정벌 가이드")
            );
            when(assistantService.answerUserInput(anyString(), anyString())).thenReturn(response);

            //when - then
            mockMvc.perform(post("/api/v1/assistants/messages")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data.answer").value("호박벌은 온실 작물의 수정에 사용됩니다."))
                    .andExpect(jsonPath("$.data.conversationId").value("conv-id"))
                    .andExpect(jsonPath("$.data.sources").isArray())
                    .andExpect(jsonPath("$.data.sources.length()").value(2))
                    .andExpect(jsonPath("$.data.sources[0]").value("농촌진흥청 양봉 매뉴얼"))
                    .andDo(print());
        }

        @Test
        @DisplayName("출처가 없는 경우 빈 목록을 반환한다.")
        void answerUserInput_emptySources() throws Exception {
            //given
            AssistantRequest request = new AssistantRequest("질문", null, null);
            AssistantResponse response = new AssistantResponse("답변", "new-conv-id", List.of());
            when(assistantService.answerUserInput(anyString(), any())).thenReturn(response);

            //when - then
            mockMvc.perform(post("/api/v1/assistants/messages")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.sources").isArray())
                    .andExpect(jsonPath("$.data.sources.length()").value(0))
                    .andDo(print());
        }
    }

}
