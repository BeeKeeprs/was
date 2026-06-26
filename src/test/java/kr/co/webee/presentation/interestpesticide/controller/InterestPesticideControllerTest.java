package kr.co.webee.presentation.interestpesticide.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.webee.application.interestpesticide.service.InterestPesticideService;
import kr.co.webee.config.TestWebConfig;
import kr.co.webee.presentation.config.WebConfig;
import kr.co.webee.presentation.interestpesticide.dto.request.InterestPesticideRegisterRequest;
import kr.co.webee.presentation.interestpesticide.dto.response.InterestPesticideRegisterResponse;
import kr.co.webee.presentation.support.resolver.UserIdArgumentResolver;
import org.junit.jupiter.api.DisplayName;
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

import kr.co.webee.presentation.interestpesticide.dto.response.InterestPesticideListResponse;
import org.junit.jupiter.api.Nested;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = InterestPesticideController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserIdArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@Import(TestWebConfig.class)
@ActiveProfiles("test")
class InterestPesticideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InterestPesticideService interestPesticideService;

    @Test
    @DisplayName("관심 농약을 등록한다.")
    void registerInterestPesticide() throws Exception {
        //given
        InterestPesticideRegisterRequest request = InterestPesticideRegisterRequest.builder()
                .pesticideApplicationNo("1-1-000001")
                .brandName("유기농바이오킬")
                .cropName("벼")
                .build();
        when(interestPesticideService.registerInterestPesticide(any(), any()))
                .thenReturn(InterestPesticideRegisterResponse.of(1L));

        //when - then
        mockMvc.perform(post("/api/v1/interest-pesticides")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andExpect(jsonPath("$.data.interestPesticideId").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("pesticideApplicationNo가 없으면 400이 반환된다.")
    void registerInterestPesticide_missingApplicationNo() throws Exception {
        //given
        InterestPesticideRegisterRequest request = InterestPesticideRegisterRequest.builder()
                .pesticideApplicationNo("")
                .build();

        //when - then
        mockMvc.perform(post("/api/v1/interest-pesticides")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Nested
    @DisplayName("관심 농약 목록 조회")
    class GetAllInterestPesticides {

        @Test
        @DisplayName("관심 농약 목록을 조회한다.")
        void getAllInterestPesticides() throws Exception {
            //given
            List<InterestPesticideListResponse> items = List.of(
                    InterestPesticideListResponse.builder()
                            .interestPesticideId(1L)
                            .pesticideApplicationNo("1-1-000001")
                            .cropName("딸기")
                            .build(),
                    InterestPesticideListResponse.builder()
                            .interestPesticideId(2L)
                            .pesticideApplicationNo("1-1-000002")
                            .cropName("벼")
                            .build()
            );
            when(interestPesticideService.getAllInterestPesticides(anyLong(), any()))
                    .thenReturn(new SliceImpl<>(items, PageRequest.of(0, 10), false));

            //when - then
            mockMvc.perform(get("/api/v1/interest-pesticides")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(2))
                    .andExpect(jsonPath("$.data.content[0].pesticideApplicationNo").value("1-1-000001"))
                    .andDo(print());
        }

        @Test
        @DisplayName("등록된 관심 농약이 없으면 빈 배열을 반환한다.")
        void getAllInterestPesticides_empty() throws Exception {
            //given
            when(interestPesticideService.getAllInterestPesticides(anyLong(), any()))
                    .thenReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 10), false));

            //when - then
            mockMvc.perform(get("/api/v1/interest-pesticides"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(0))
                    .andDo(print());
        }
    }
}
