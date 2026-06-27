package kr.co.webee.presentation.community.controller;

import kr.co.webee.application.community.service.CommunityService;
import kr.co.webee.config.TestWebConfig;
import kr.co.webee.domain.post.type.PostCategory;
import kr.co.webee.presentation.community.dto.response.ActiveUserResponse;
import kr.co.webee.presentation.community.dto.response.TrendingCategoryResponse;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestWebConfig.class)
@WebMvcTest(
        controllers = CommunityController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserIdArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@ActiveProfiles("test")
class CommunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommunityService communityService;

    @Nested
    @DisplayName("활동 중인 유저 목록 조회")
    class GetActiveUsers {

        @Test
        @DisplayName("활동 중인 유저 목록을 조회한다.")
        void getActiveUsers() throws Exception {
            //given
            List<ActiveUserResponse> response = List.of(
                    ActiveUserResponse.builder()
                            .userId(1L)
                            .name("홍길동")
                            .profileImageUrl("https://example.com/profile1.jpg")
                            .build(),
                    ActiveUserResponse.builder()
                            .userId(2L)
                            .name("김철수")
                            .profileImageUrl(null)
                            .build()
            );
            when(communityService.getActiveUsers()).thenReturn(response);

            //when - then
            mockMvc.perform(get("/api/v1/community/active-users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].userId").value(1L))
                    .andExpect(jsonPath("$.data[0].name").value("홍길동"))
                    .andExpect(jsonPath("$.data[0].profileImageUrl").value("https://example.com/profile1.jpg"))
                    .andExpect(jsonPath("$.data[1].userId").value(2L))
                    .andExpect(jsonPath("$.data[1].name").value("김철수"))
                    .andDo(print());
        }

        @Test
        @DisplayName("활동 중인 유저가 없으면 빈 배열을 반환한다.")
        void getActiveUsersEmpty() throws Exception {
            //given
            when(communityService.getActiveUsers()).thenReturn(List.of());

            //when - then
            mockMvc.perform(get("/api/v1/community/active-users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(0))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("지금 뜨는 주제 조회")
    class GetTrendingCategories {

        @Test
        @DisplayName("지금 뜨는 주제 목록을 조회한다.")
        void getTrendingCategories() throws Exception {
            //given
            List<TrendingCategoryResponse> response = List.of(
                    TrendingCategoryResponse.builder()
                            .category(PostCategory.KNOWHOW)
                            .postCount(14)
                            .build(),
                    TrendingCategoryResponse.builder()
                            .category(PostCategory.QUESTION)
                            .postCount(9)
                            .build()
            );
            when(communityService.getTrendingCategories()).thenReturn(response);

            //when - then
            mockMvc.perform(get("/api/v1/community/trending-categories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].category").value("KNOWHOW"))
                    .andExpect(jsonPath("$.data[0].postCount").value(14))
                    .andExpect(jsonPath("$.data[1].category").value("QUESTION"))
                    .andExpect(jsonPath("$.data[1].postCount").value(9))
                    .andDo(print());
        }

        @Test
        @DisplayName("뜨는 주제가 없으면 빈 배열을 반환한다.")
        void getTrendingCategoriesEmpty() throws Exception {
            //given
            when(communityService.getTrendingCategories()).thenReturn(List.of());

            //when - then
            mockMvc.perform(get("/api/v1/community/trending-categories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(0))
                    .andDo(print());
        }
    }
}
