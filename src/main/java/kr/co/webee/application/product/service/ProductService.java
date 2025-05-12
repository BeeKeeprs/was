package kr.co.webee.application.product.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.domain.product.entity.Product;
import kr.co.webee.domain.product.entity.ProductImage;
import kr.co.webee.domain.product.repository.ProductImageRepository;
import kr.co.webee.domain.product.repository.ProductRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.storage.FileStorage;
import kr.co.webee.presentation.product.dto.request.ProductCreateRequest;
import kr.co.webee.presentation.product.dto.request.ProductUpdateRequest;
import kr.co.webee.presentation.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorage fileStorage;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createProduct(ProductCreateRequest request, List<MultipartFile> images, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

        Product product = Product.builder()
                .price(request.price())
                .beeType(request.beeType())
                .content(request.content())
                .seller(seller)
                .build();

        productRepository.save(product);

        for (MultipartFile image : images) {
            String imageUrl = fileStorage.upload(image); // 외부 스토리지에 업로드 (S3 등)
            ProductImage productImage = ProductImage.builder()
                    .imageUrl(imageUrl)
                    .product(product)
                    .build();
            productImageRepository.save(productImage);
        }

        return product.getId();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        List<String> imageUrls = productImageRepository.findAllByProductId(productId).stream()
                .map(ProductImage::getImageUrl)
                .toList();

        return ProductResponse.from(product, imageUrls);
    }

    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequest dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));

        product.update(dto.price(), dto.beeType(), dto.content());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));

        productRepository.delete(product);
    }
}
