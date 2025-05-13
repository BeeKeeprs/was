package kr.co.webee.presentation.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.presentation.product.dto.request.ProductReviewCreateRequest;
import kr.co.webee.presentation.product.dto.request.ProductReviewUpdateRequest;
import kr.co.webee.presentation.product.dto.response.ProductReviewResponse;

import java.util.List;

@Tag(name = "Product Review", description = "상품 후기 관련 API")
public interface ProductReviewApi {

    @Operation(summary = "리뷰 생성", description = "상품에 리뷰를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "리뷰 생성 완료")
    Long createReview(Long productId, ProductReviewCreateRequest request, Long userId);

    @Operation(summary = "리뷰 단건 조회", description = "리뷰 ID로 리뷰를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ProductReviewResponse getReview(Long reviewId);

    @Operation(summary = "상품별 리뷰 목록 조회", description = "특정 상품에 대한 리뷰 리스트를 조회합니다.")
    List<ProductReviewResponse> getReviewsByProduct(Long productId);

    @Operation(summary = "리뷰 수정", description = "리뷰 내용을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 완료")
    String updateReview(Long reviewId, ProductReviewUpdateRequest request);

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 완료")
    String deleteReview(Long reviewId);
}
