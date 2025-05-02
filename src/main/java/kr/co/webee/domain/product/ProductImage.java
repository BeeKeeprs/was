package kr.co.webee.domain.product;

import jakarta.persistence.*;
import kr.co.webee.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public ProductImage(String imageUrl, Product product) {
        if (!StringUtils.hasText(imageUrl)) {
            throw new IllegalArgumentException("imageUrl은 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.imageUrl = imageUrl;
        this.product = Objects.requireNonNull(product, "product는 null이 될 수 없습니다.");

    }
}
