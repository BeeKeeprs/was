package kr.co.webee.domain.product.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

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
    public Product(String name, Integer price, BeeType beeType, String content, User seller) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.name = name;
        this.price = Objects.requireNonNull(price, "price는 null이 될 수 없습니다.");
        this.beeType = Objects.requireNonNull(beeType, "beeType은 null이 될 수 없습니다.");
        this.content = content;
        this.seller = Objects.requireNonNull(seller, "seller는 null이 될 수 없습니다.");
    }

    public void update(String name, Integer price, BeeType beeType, String content) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.name = name;
        this.price = Objects.requireNonNull(price, "price는 null이 될 수 없습니다.");
        this.beeType = Objects.requireNonNull(beeType, "beeType은 null이 될 수 없습니다.");
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.content = content;
    }
}
