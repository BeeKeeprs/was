package kr.co.webee.application.product.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.product.entity.Product;
import kr.co.webee.domain.product.entity.ProductImage;
import kr.co.webee.domain.product.repository.ProductImageRepository;
import kr.co.webee.domain.product.repository.ProductRepository;
import kr.co.webee.domain.profile.business.entity.Business;
import kr.co.webee.domain.profile.business.repository.BusinessRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.product.dto.request.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSaverService {

    private final BusinessRepository businessRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public Long save(ProductCreateRequest request, List<String> imageUrls) {
        Business business = businessRepository.findById(request.businessId())
                .orElseThrow(() -> new EntityNotFoundException("Not found Business"));

        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .beeType(request.beeType())
                .content(request.content())
                .origin(request.origin())
                .transactionType(request.transactionType())
                .transactionMethod(request.transactionMethod())
                .business(business)
                .build();

        productRepository.save(product);

        List<ProductImage> productImages = imageUrls.stream()
                .map(url -> ProductImage.builder().imageUrl(url).product(product).build())
                .toList();
        productImageRepository.saveAll(productImages);

        return product.getId();
    }
}
