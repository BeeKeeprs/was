package kr.co.webee.presentation.hive.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.webee.application.hive.dto.request.HiveGateActionRegisterRequest;
import kr.co.webee.application.hive.dto.request.HiveGateActionUpdateRequest;
import kr.co.webee.application.hive.dto.response.HiveGateActionDetailResponse;
import kr.co.webee.application.hive.dto.response.HiveGateActionListResponse;
import kr.co.webee.application.hive.dto.response.HiveGateActionRegisterResponse;
import kr.co.webee.application.hive.service.HiveGateActionService;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.config.TestWebConfig;
import kr.co.webee.domain.hive.type.GateActionType;
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

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestWebConfig.class)
@WebMvcTest(
        controllers = HiveGateActionController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserIdArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@ActiveProfiles("test")
class HiveGateActionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HiveGateActionService hiveGateActionService;

    @Nested
    @DisplayName("개폐기 동작 등록")
    class RegisterHiveGateAction {

        @Test
        @DisplayName("개폐기 동작을 등록한다.")
        void registerHiveGateAction() throws Exception {
            //given
            HiveGateActionRegisterRequest request = new HiveGateActionRegisterRequest(
                    "새벽 환기", GateActionType.OPEN_ONLY, LocalTime.of(9, 0), false
            );
            when(hiveGateActionService.registerHiveGateAction(anyLong(), anyLong(), any()))
                    .thenReturn(new HiveGateActionRegisterResponse(1L));

            //when - then
            mockMvc.perform(post("/api/v1/hives/{hiveId}/gate/actions", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data.id").value(1L))
                    .andDo(print());
        }

        @Test
        @DisplayName("제목 없이 개폐기 동작을 등록하려는 경우 400을 반환한다.")
        void registerWithBlankTitle() throws Exception {
            //given
            HiveGateActionRegisterRequest request = new HiveGateActionRegisterRequest(
                    "", GateActionType.OPEN_ONLY, LocalTime.of(9, 0), false
            );

            //when - then
            mockMvc.perform(post("/api/v1/hives/{hiveId}/gate/actions", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorType.FAILED_VALIDATION.getCode()))
                    .andDo(print());
        }

        @Test
        @DisplayName("동작 유형 없이 개폐기 동작을 등록하려는 경우 400을 반환한다.")
        void registerWithNullActionType() throws Exception {
            //given
            HiveGateActionRegisterRequest request = new HiveGateActionRegisterRequest(
                    "새벽 환기", null, LocalTime.of(9, 0), false
            );

            //when - then
            mockMvc.perform(post("/api/v1/hives/{hiveId}/gate/actions", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorType.FAILED_VALIDATION.getCode()))
                    .andDo(print());
        }

        @Test
        @DisplayName("동작 시각 없이 개폐기 동작을 등록하려는 경우 400을 반환한다.")
        void registerWithNullActionTime() throws Exception {
            //given
            HiveGateActionRegisterRequest request = new HiveGateActionRegisterRequest(
                    "새벽 환기", GateActionType.OPEN_ONLY, null, false
            );

            //when - then
            mockMvc.perform(post("/api/v1/hives/{hiveId}/gate/actions", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorType.FAILED_VALIDATION.getCode()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("개폐기 동작 목록 조회")
    class GetAllHiveGateActionList {

        @Test
        @DisplayName("개폐기 동작 목록을 조회한다.")
        void getAllHiveGateActionList() throws Exception {
            //given
            List<HiveGateActionListResponse> response = List.of(
                    new HiveGateActionListResponse(1L, "아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), true),
                    new HiveGateActionListResponse(2L, "저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), false)
            );
            when(hiveGateActionService.getAllHiveGateActionList(anyLong(), anyLong())).thenReturn(response);

            //when - then
            mockMvc.perform(get("/api/v1/hives/{hiveId}/gate/actions", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andDo(print());
        }

        @Test
        @DisplayName("등록된 개폐기 동작이 없으면 빈 배열을 반환한다.")
        void getAllHiveGateActionListEmpty() throws Exception {
            //given
            when(hiveGateActionService.getAllHiveGateActionList(anyLong(), anyLong())).thenReturn(List.of());

            //when - then
            mockMvc.perform(get("/api/v1/hives/{hiveId}/gate/actions", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(0))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("개폐기 동작 단건 조회")
    class GetHiveGateAction {

        @Test
        @DisplayName("개폐기 동작을 단건 조회한다.")
        void getHiveGateAction() throws Exception {
            //given
            HiveGateActionDetailResponse response = new HiveGateActionDetailResponse(
                    1L, "아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), true
            );
            when(hiveGateActionService.getHiveGateAction(anyLong(), anyLong(), anyLong())).thenReturn(response);

            //when - then
            mockMvc.perform(get("/api/v1/hives/{hiveId}/gate/actions/{actionId}", 1L, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data.id").value(1L))
                    .andExpect(jsonPath("$.data.title").value("아침 열기"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("개폐기 동작 수정")
    class UpdateHiveGateAction {

        @Test
        @DisplayName("개폐기 동작을 수정한다.")
        void updateHiveGateAction() throws Exception {
            //given
            HiveGateActionUpdateRequest request = new HiveGateActionUpdateRequest(
                    "저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), true
            );
            HiveGateActionDetailResponse response = new HiveGateActionDetailResponse(
                    1L, "저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), true
            );
            when(hiveGateActionService.updateHiveGateAction(anyLong(), anyLong(), anyLong(), any())).thenReturn(response);

            //when - then
            mockMvc.perform(patch("/api/v1/hives/{hiveId}/gate/actions/{actionId}", 1L, 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data.title").value("저녁 닫기"))
                    .andExpect(jsonPath("$.data.actionType").value("CLOSE_ONLY"))
                    .andDo(print());
        }

        @Test
        @DisplayName("제목 없이 개폐기 동작을 수정하려는 경우 400을 반환한다.")
        void updateWithBlankTitle() throws Exception {
            //given
            HiveGateActionUpdateRequest request = new HiveGateActionUpdateRequest(
                    "", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), false
            );

            //when - then
            mockMvc.perform(patch("/api/v1/hives/{hiveId}/gate/actions/{actionId}", 1L, 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorType.FAILED_VALIDATION.getCode()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("개폐기 동작 삭제")
    class DeleteHiveGateAction {

        @Test
        @DisplayName("개폐기 동작을 삭제한다.")
        void deleteHiveGateAction() throws Exception {
            //when - then
            mockMvc.perform(delete("/api/v1/hives/{hiveId}/gate/actions/{actionId}", 1L, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data").value("OK"))
                    .andDo(print());
        }
    }
}
