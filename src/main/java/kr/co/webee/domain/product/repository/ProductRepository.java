package kr.co.webee.domain.product.repository;

import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Slice<Product> findByBeeType(BeeType beeType, Pageable pageable);
}
