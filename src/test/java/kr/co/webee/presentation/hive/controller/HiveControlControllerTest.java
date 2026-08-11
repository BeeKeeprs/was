package kr.co.webee.presentation.hive.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.webee.application.hive.dto.response.HiveControlListResponse;
import kr.co.webee.application.hive.service.HiveControlService;
import kr.co.webee.config.TestWebConfig;
import kr.co.webee.domain.hive.type.ControlType;
import kr.co.webee.presentation.config.WebConfig;
import kr.co.webee.presentation.hive.dto.request.HiveManualControlRequest;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestWebConfig.class)
@WebMvcTest(
        controllers = HiveControlController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserIdArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@ActiveProfiles("test")
class HiveControlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HiveControlService hiveControlService;

    @Nested
    @DisplayName("제어 설정 목록 조회")
    class GetControlList {

        @Test
        @DisplayName("제어 설정 목록을 조회한다.")
        void getControlList() throws Exception {
            //given
            HiveControlListResponse response = new HiveControlListResponse(List.of(
                    new HiveControlListResponse.ControlSetting(ControlType.TEMPERATURE, 36.5),
                    new HiveControlListResponse.ControlSetting(ControlType.HUMIDITY, 60.0)
            ));
            when(hiveControlService.getControlList(anyLong(), anyLong())).thenReturn(response);

            //when - then
            mockMvc.perform(get("/api/v1/hives/{hiveId}/control", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data.controls").isArray())
                    .andExpect(jsonPath("$.data.controls.length()").value(2))
                    .andDo(print());
        }

        @Test
        @DisplayName("등록된 제어 설정이 없으면 빈 배열을 반환한다.")
        void getControlListEmpty() throws Exception {
            //given
            HiveControlListResponse response = new HiveControlListResponse(List.of());
            when(hiveControlService.getControlList(anyLong(), anyLong())).thenReturn(response);

            //when - then
            mockMvc.perform(get("/api/v1/hives/{hiveId}/control", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.controls").isArray())
                    .andExpect(jsonPath("$.data.controls.length()").value(0))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("수동 제어 명령 전송")
    class SetManualControl {

        @Test
        @DisplayName("수동 제어 명령을 전송한다.")
        void setManualControl() throws Exception {
            //given
            HiveManualControlRequest request = new HiveManualControlRequest(36.5, 60.0);

            //when - then
            mockMvc.perform(post("/api/v1/hives/{hiveId}/control/manual", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data").value("OK"))
                    .andDo(print());
        }
    }
}
