package kr.co.webee.domain.product;

import jakarta.persistence.*;
import kr.co.webee.domain.bee.BeeType;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BeeType beeType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Builder
    public Product(Integer price, BeeType beeType, String content, User seller) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.price = Objects.requireNonNull(price, "price는 null이 될 수 없습니다.");
        this.beeType = Objects.requireNonNull(beeType, "beeType은 null이 될 수 없습니다.");
        this.content = content;
        this.seller = Objects.requireNonNull(seller, "seller는 null이 될 수 없습니다.");
    }
}
