package kr.co.webee.presentation.product.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.product.service.ProductReviewService;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.product.api.ProductReviewApi;
import kr.co.webee.presentation.product.dto.request.ProductReviewCreateRequest;
import kr.co.webee.presentation.product.dto.request.ProductReviewUpdateRequest;
import kr.co.webee.presentation.product.dto.response.ProductReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products/reviews")
public class ProductReviewController implements ProductReviewApi {

    private final ProductReviewService reviewService;

    @PostMapping("/{productId}")
    public Long createReview(
            @PathVariable Long productId,
            @RequestBody @Valid ProductReviewCreateRequest request,
            @UserId Long userId) {
        return reviewService.createReview(productId, userId, request);
    }

    @GetMapping("/{reviewId}")
    public ProductReviewResponse getReview(@PathVariable Long reviewId) {
        return reviewService.getReview(reviewId);
    }

    @GetMapping("/product/{productId}")
    public List<ProductReviewResponse> getReviewsByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProduct(productId);
    }

    @PutMapping("/{reviewId}")
    public String updateReview(@PathVariable Long reviewId, @RequestBody @Valid ProductReviewUpdateRequest request) {
        reviewService.updateReview(reviewId, request);
        return "OK";
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return "OK";
    }
}
