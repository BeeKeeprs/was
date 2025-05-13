package kr.co.webee.application.product.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.domain.product.entity.Product;
import kr.co.webee.domain.product.entity.ProductReview;
import kr.co.webee.domain.product.repository.ProductRepository;
import kr.co.webee.domain.product.repository.ProductReviewRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.product.dto.request.ProductReviewCreateRequest;
import kr.co.webee.presentation.product.dto.request.ProductReviewUpdateRequest;
import kr.co.webee.presentation.product.dto.response.ProductReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductReviewRepository reviewRepository;

    @Transactional
    public Long createReview(Long productId, Long userId, ProductReviewCreateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ProductReview review = ProductReview.builder()
                .content(request.content())
                .product(product)
                .user(user)
                .build();

        return reviewRepository.save(review).getId();
    }

    @Transactional(readOnly = true)
    public ProductReviewResponse getReview(Long reviewId) {
        return ProductReviewResponse.from(
                reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new EntityNotFoundException("Review not found"))
        );
    }

    @Transactional(readOnly = true)
    public List<ProductReviewResponse> getReviewsByProduct(Long productId) {
        return reviewRepository.findAllByProductId(productId).stream()
                .map(ProductReviewResponse::from)
                .toList();
    }

    @Transactional
    public void updateReview(Long reviewId, ProductReviewUpdateRequest request) {
        ProductReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        review.updateContent(request.content());
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        ProductReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        reviewRepository.delete(review);
    }
}
