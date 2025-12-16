package kr.co.webee.domain.product.repository;

import kr.co.webee.domain.annotation.RepositoryTest;
import kr.co.webee.domain.product.entity.Product;
import kr.co.webee.domain.product.entity.ProductReview;
import kr.co.webee.domain.profile.business.entity.Business;
import kr.co.webee.domain.profile.business.repository.BusinessRepository;
import kr.co.webee.domain.profile.crop.entity.Coordinates;
import kr.co.webee.domain.profile.crop.entity.Location;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
class ProductReviewRepositoryTest {
    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @DisplayName("상품 id로 해당 상품의 모든 리뷰를 조회한다.")
    @Test
    void findAllByProductId() {
        //given
        User owner = User.builder().username("ownerUsername").password("ownerPassword").name("ownerName").businessRegistered(false).build();
        userRepository.save(owner);

        Location location = Location.builder().address("경북 경산시 대학로280").coordinates(Coordinates.builder().latitude(123.34).longitude(67.43).build()).build();
        Business business = Business.builder().registrationNumber("12345678").representativeName("홍길동").commencementDate(LocalDate.of(2021, 9, 12)).companyName("허니 양봉").businessLocation(location).phoneNumber("01012345678").user(owner).build();
        businessRepository.save(business);

        Product product1 = createProduct(business);
        Product product2 = createProduct(business);
        productRepository.saveAll(List.of(product1, product2));

        User reviewer = User.builder().username("reviewerUsername").password("reviewerPassword").name("reviewerName").businessRegistered(false).build();
        userRepository.save(reviewer);

        ProductReview productReview1 = createProductReview("리뷰 내용1", product1, reviewer);
        ProductReview productReview2 = createProductReview("리뷰 내용2", product1, reviewer);
        ProductReview productReview3 = createProductReview("리뷰 내용3", product2, reviewer);
        ProductReview productReview4 = createProductReview("리뷰 내용4", product2, reviewer);
        productReviewRepository.saveAll(List.of(productReview1, productReview2, productReview3, productReview4));

        //when
        List<ProductReview> products = productReviewRepository.findAllByProductId(product1.getId());

        //then
        assertThat(products).hasSize(2)
                .extracting("content")
                .containsExactlyInAnyOrder("리뷰 내용1", "리뷰 내용2");
    }

    private ProductReview createProductReview(String content, Product product, User reviewer) {
        return ProductReview.builder()
                .content(content)
                .product(product)
                .user(reviewer)
                .build();
    }

    private Product createProduct(Business business) {
        return Product.builder()
                .name("상품 제목")
                .price(1000)
                .beeType(MASON_BEE)
                .content("내용")
                .origin(DOMESTIC)
                .transactionType(PURCHASE)
                .transactionMethod(OFFLINE)
                .business(business)
                .build();
    }
}