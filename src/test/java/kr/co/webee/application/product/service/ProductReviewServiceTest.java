package kr.co.webee.application.product.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.domain.product.entity.Product;
import kr.co.webee.domain.product.entity.ProductReview;
import kr.co.webee.domain.product.repository.ProductRepository;
import kr.co.webee.domain.product.repository.ProductReviewRepository;
import kr.co.webee.domain.profile.business.entity.Business;
import kr.co.webee.domain.profile.business.repository.BusinessRepository;
import kr.co.webee.domain.profile.crop.entity.Coordinates;
import kr.co.webee.domain.profile.crop.entity.Location;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.product.dto.request.ProductReviewCreateRequest;
import kr.co.webee.presentation.product.dto.request.ProductReviewUpdateRequest;
import kr.co.webee.presentation.product.dto.response.ProductReviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static kr.co.webee.domain.bee.type.BeeType.MASON_BEE;
import static kr.co.webee.domain.product.enums.Origin.DOMESTIC;
import static kr.co.webee.domain.product.enums.TransactionMethod.OFFLINE;
import static kr.co.webee.domain.product.enums.TransactionType.PURCHASE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class ProductReviewServiceTest {

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    private User reviewer;

    @BeforeEach
    void setUp() {
        User owner = User.builder().username("ownerUsername").password("ownerPassword").name("ownerName").businessRegistered(false).build();
        userRepository.save(owner);

        Location location = Location.builder().address("경북 경산시 대학로280").coordinates(Coordinates.builder().latitude(123.34).longitude(67.43).build()).build();
        Business business = Business.builder().registrationNumber("12345678").representativeName("홍길동").commencementDate(LocalDate.of(2021, 9, 12)).companyName("허니 양봉").businessLocation(location).phoneNumber("01012345678").user(owner).build();
        businessRepository.save(business);

        product = Product.builder().name("상품 제목").price(1000).beeType(MASON_BEE).content("내용").origin(DOMESTIC).transactionType(PURCHASE).transactionMethod(OFFLINE).business(business).build();
        productRepository.save(product);

        reviewer = User.builder().username("reviewerUsername").password("reviewerPassword").name("reviewerName").businessRegistered(false).build();
        userRepository.save(reviewer);
    }

    @DisplayName("상품 리뷰를 등록한다.")
    @Test
    void createReview() {
        //given
        ProductReviewCreateRequest request = new ProductReviewCreateRequest("리뷰 내용");

        //when
        Long reviewId = productReviewService.createReview(product.getId(), reviewer.getId(), request);

        //then
        assertThat(reviewId).isNotNull();

        ProductReview productReview = productReviewRepository.findById(reviewId).orElseThrow();
        assertThat(productReview.getContent()).isEqualTo(request.content());
        assertThat(productReview.getProduct().getId()).isEqualTo(product.getId());
        assertThat(productReview.getUser().getId()).isEqualTo(reviewer.getId());
    }

    @DisplayName("존재하지 않는 상품에 대해 상품 리뷰를 등록하려는 경우 예외가 발생한다.")
    @Test
    void createReviewWithNoProduct() {
        //given
        ProductReviewCreateRequest request = new ProductReviewCreateRequest("리뷰 내용");
        Long notExistsProductId = 10L;

        //when - then
        assertThatThrownBy(() -> productReviewService.createReview(notExistsProductId, reviewer.getId(), request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product not found");
    }

    @DisplayName("상품 리뷰 등록 시 리뷰 작성자가 등록되지 않은 사용자인 경우 예외가 발생한다.")
    @Test
    void createReviewWithNoUser() {
        //given
        ProductReviewCreateRequest request = new ProductReviewCreateRequest("리뷰 내용");
        Long notExistsUserId = 10L;

        //when - then
        assertThatThrownBy(() -> productReviewService.createReview(product.getId(), notExistsUserId, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found");
    }

    @DisplayName("상품 리뷰 id로 상품 리뷰를 조회한다.")
    @Test
    void getReview() {
        //given
        ProductReview review = createProductReview("리뷰 내용", product);
        ProductReview savedReview = productReviewRepository.save(review);

        //when
        ProductReviewResponse response = productReviewService.getReview(savedReview.getId());

        //then
        assertThat(response)
                .extracting("content", "productId")
                .contains("리뷰 내용", product.getId());
        assertThat(response.writer())
                .extracting("id", "name")
                .contains(reviewer.getId(), reviewer.getName());
    }

    @DisplayName("등록되지 않은 상품 리뷰를 조회할 경우 예외가 발생한다.")
    @Test
    void getReviewWhenReviewIsNotExists() {
        //given
        Long notExistsReviewId = 10L;

        //when - then
        assertThatThrownBy(() -> productReviewService.getReview(notExistsReviewId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Review not found");
    }


    @DisplayName("특정 상품에 등록된 리뷰 리스트를 조회한다.")
    @Test
    void getReviewsByProduct() {
        //given
        ProductReview review1 = createProductReview("리뷰 내용1", product);
        ProductReview savedReview1 = productReviewRepository.save(review1);
        ProductReview review2 = createProductReview("리뷰 내용2", product);
        ProductReview savedReview2 = productReviewRepository.save(review2);

        //when
        List<ProductReviewResponse> reviews = productReviewService.getReviewsByProduct(product.getId());

        //then
        assertThat(reviews).hasSize(2)
                .extracting("content")
                .containsExactly("리뷰 내용1", "리뷰 내용2");
    }

    @DisplayName("기존에 등록한 상품 리뷰를 수정한다.")
    @Test
    void updateReview() {
        //given
        ProductReview review = createProductReview("리뷰 내용", product);
        ProductReview savedReview = productReviewRepository.save(review);

        ProductReviewUpdateRequest updateRequest = new ProductReviewUpdateRequest("리뷰 수정");

        //when
        productReviewService.updateReview(savedReview.getId(), updateRequest);

        //then
        ProductReview updatedReview = productReviewRepository.findById(review.getId()).orElseThrow();
        assertThat(updatedReview.getContent()).isEqualTo("리뷰 수정");
    }

    @DisplayName("등록되지 않은 상품 리뷰로 수정을 시도할 경우 예외가 발생한다.")
    @Test
    void updateReviewWhenReviewIsNotExists() {
        //given
        Long notExistsReviewId = 10L;
        ProductReviewUpdateRequest updateRequest = new ProductReviewUpdateRequest("리뷰 수정");

        //when - then
        assertThatThrownBy(() -> productReviewService.updateReview(notExistsReviewId, updateRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Review not found");
    }

    @DisplayName("기존에 등록한 상품 리뷰를 삭제한다.")
    @Test
    void deleteReview() {
        //given
        ProductReviewCreateRequest request = new ProductReviewCreateRequest("리뷰 내용");
        Long reviewId = productReviewService.createReview(product.getId(), reviewer.getId(), request);

        //when
        productReviewService.deleteReview(reviewId);

        //then
        assertThat(productReviewRepository.existsById(reviewId)).isFalse();
    }

    @DisplayName("등록되지 않은 상품 리뷰로 삭제를 시도할 경우 예외가 발생한다.")
    @Test
    void deleteReviewWhenReviewIsNotExists() {
        //given
        Long notExistsReviewId = 10L;

        //when - then
        assertThatThrownBy(() -> productReviewService.deleteReview(notExistsReviewId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Review not found");
    }

    private ProductReview createProductReview(String content, Product product) {
        return ProductReview.builder()
                .content(content)
                .product(product)
                .user(reviewer)
                .build();
    }
}
