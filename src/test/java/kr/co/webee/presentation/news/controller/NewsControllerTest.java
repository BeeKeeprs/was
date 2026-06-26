package kr.co.webee.presentation.news.controller;

import kr.co.webee.application.news.service.NewsService;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.config.TestWebConfig;
import kr.co.webee.presentation.config.WebConfig;
import kr.co.webee.presentation.news.dto.response.NewsArticleDetailResponse;
import kr.co.webee.presentation.news.dto.response.NewsArticleListResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestWebConfig.class)
@WebMvcTest(
        controllers = NewsController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserIdArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@ActiveProfiles("test")
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NewsService newsService;

    @Nested
    @DisplayName("뉴스 기사 목록 조회")
    class GetAllNewsArticleList {

        @Test
        @DisplayName("키워드에 해당하는 뉴스 기사 목록을 조회한다.")
        void getAllNewsArticleList() throws Exception {
            //given
            List<NewsArticleListResponse> articles = List.of(
                    new NewsArticleListResponse(1L, "꿀벌 관련 뉴스", "농민신문", LocalDateTime.of(2026, 6, 16, 9, 0)),
                    new NewsArticleListResponse(2L, "양봉 관련 뉴스", "한국농어민신문", LocalDateTime.of(2026, 6, 15, 9, 0))
            );
            when(newsService.getAllNewsArticleList(anyString(), any()))
                    .thenReturn(new SliceImpl<>(articles, PageRequest.of(0, 5), false));

            //when - then
            mockMvc.perform(get("/api/v1/news")
                            .param("keyword", "꿀벌")
                            .param("page", "0")
                            .param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(2))
                    .andExpect(jsonPath("$.data.content[0].newsArticleId").value(1L))
                    .andExpect(jsonPath("$.data.content[0].title").value("꿀벌 관련 뉴스"))
                    .andDo(print());
        }

        @Test
        @DisplayName("해당 키워드의 기사가 없으면 빈 목록을 반환한다.")
        void getAllNewsArticleListEmpty() throws Exception {
            //given
            when(newsService.getAllNewsArticleList(anyString(), any()))
                    .thenReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 5), false));

            //when - then
            mockMvc.perform(get("/api/v1/news")
                            .param("keyword", "꿀벌"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(0))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("뉴스 기사 상세 조회")
    class GetNewsArticleDetail {

        @Test
        @DisplayName("뉴스 기사 상세를 조회한다.")
        void getNewsArticleDetail() throws Exception {
            //given
            NewsArticleDetailResponse response = new NewsArticleDetailResponse(
                    1L, "수정벌 방사 시기, 기온 체크가 관건",
                    "과수 농가에서 수정벌 방사 전 확인해야 할 사항...",
                    "농민신문", LocalDateTime.of(2026, 6, 16, 9, 0)
            );
            when(newsService.getNewsArticleDetail(anyLong())).thenReturn(response);

            //when - then
            mockMvc.perform(get("/api/v1/news/{newsArticleId}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                    .andExpect(jsonPath("$.data.newsArticleId").value(1L))
                    .andExpect(jsonPath("$.data.title").value("수정벌 방사 시기, 기온 체크가 관건"))
                    .andExpect(jsonPath("$.data.source").value("농민신문"))
                    .andDo(print());
        }

        @Test
        @DisplayName("존재하지 않는 뉴스 기사를 조회하면 404를 반환한다.")
        void getNewsArticleDetailNotFound() throws Exception {
            //given
            when(newsService.getNewsArticleDetail(anyLong()))
                    .thenThrow(new BusinessException(ErrorType.ENTITY_NOT_FOUND));

            //when - then
            mockMvc.perform(get("/api/v1/news/{newsArticleId}", -1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorType.ENTITY_NOT_FOUND.getCode()))
                    .andDo(print());
        }
    }
}
