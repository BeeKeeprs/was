package kr.co.webee.presentation.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.webee.application.product.service.ProductReviewService;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.config.TestWebConfig;
import kr.co.webee.presentation.config.WebConfig;
import kr.co.webee.presentation.product.dto.request.ProductReviewCreateRequest;
import kr.co.webee.presentation.product.dto.request.ProductReviewUpdateRequest;
import kr.co.webee.presentation.product.dto.response.ProductReviewResponse;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestWebConfig.class)
@WebMvcTest(
        controllers = ProductReviewController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserIdArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@ActiveProfiles("test")
class ProductReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductReviewController productReviewController;

    @MockitoBean
    private ProductReviewService productReviewService;

    @DisplayName("상품 리뷰를 등록한다.")
    @Test
    void createReview() throws Exception {
        //given
        ProductReviewCreateRequest request = new ProductReviewCreateRequest("리뷰 내용");
        Long productId = 1L;

        //when - then
        mockMvc.perform(
                        post("/api/v1/products/reviews/{productId}", productId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andDo(print());
    }

    @DisplayName("상품 리뷰 등록 시 내용은 필수 값이다.")
    @Test
    void createReviewWithoutContent() throws Exception {
        //given
        ProductReviewCreateRequest request = new ProductReviewCreateRequest("");
        Long productId = 1L;

        //when - then
        mockMvc.perform(
                post("/api/v1/products/reviews/{productId}", productId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorType.FAILED_VALIDATION.getCode()))
                .andDo(print());
    }

    @DisplayName("상품 리뷰를 상세 조회한다.")
    @Test
    void getReview() throws Exception {
        //given
        Long reviewId=1L;

        //when - then
        mockMvc.perform(
                get("/api/v1/products/reviews/{reviewId}",reviewId)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andDo(print());
    }

    @DisplayName("특정 상품의 리뷰 목록을 조회한다.")
    @Test
    void getReviewsByProduct() throws Exception {
        //given
        Long productId=1L;
        List<ProductReviewResponse> result = List.of();
        when(productReviewService.getReviewsByProduct(1L)).thenReturn(result);

        //when - then
        mockMvc.perform(
                get("/api/v1/products/reviews/product/{productId}",productId)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @DisplayName("상품 리뷰를 수정한다.")
    @Test
    void updateReview() throws Exception {
        //given
        ProductReviewUpdateRequest request = new ProductReviewUpdateRequest("리뷰 내용 수정");
        Long reviewId = 1L;

        //when - then
        mockMvc.perform(
                put("/api/v1/products/reviews/{reviewId}",reviewId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andDo(print());
    }

    @DisplayName("상품 리뷰 수정 시 내용은 필수 값이다.")
    @Test
    void updateReviewWithoutContent() throws Exception {
        //given
        ProductReviewCreateRequest request = new ProductReviewCreateRequest("");
        Long reviewId = 1L;

        //when - then
        mockMvc.perform(
                put("/api/v1/products/reviews/{reviewId}",reviewId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorType.FAILED_VALIDATION.getCode()));
    }

    @DisplayName("상품 리뷰를 삭제한다.")
    @Test
    void deleteReview() throws Exception {
        //given
        Long reviewId = 1L;

        //when - then
        mockMvc.perform(
                delete("/api/v1/products/reviews/{reviewId}",reviewId)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andDo(print());
    }
}

