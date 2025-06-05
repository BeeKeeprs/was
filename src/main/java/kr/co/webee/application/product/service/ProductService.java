package kr.co.webee.application.product.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.product.entity.Product;
import kr.co.webee.domain.product.entity.ProductImage;
import kr.co.webee.domain.product.repository.ProductImageRepository;
import kr.co.webee.domain.product.repository.ProductRepository;
import kr.co.webee.infrastructure.storage.FileStorageClient;
import kr.co.webee.presentation.product.dto.request.ProductCreateRequest;
import kr.co.webee.presentation.product.dto.request.ProductUpdateRequest;
import kr.co.webee.presentation.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageClient fileStorageClient;
    private final ProductImageRepository productImageRepository;
    private final ProductSaverService productSaverService;

    @Transactional(readOnly = true)
    public Slice<ProductResponse> getAllProducts(Pageable pageable, BeeType beeType) {
        Slice<Product> products = beeType == null ? productRepository.findAll(pageable) : productRepository.findByBeeType(beeType, pageable);

        return products.map(product -> {
            List<String> imageUrls = productImageRepository.findAllByProductId(product.getId())
                    .stream()
                    .map(ProductImage::getImageUrl)
                    .toList();

            return ProductResponse.from(product, imageUrls);
        });
    }

    public Long createProduct(ProductCreateRequest request, List<MultipartFile> images) {
        String prefix = "products/" + request.businessId();

        System.out.println("images = " + images);

        List<String> imageUrls = Optional.ofNullable(images).orElseGet(List::of).stream()
                .map(image -> fileStorageClient.upload(image, prefix))
                .toList();

        return productSaverService.save(request, imageUrls);
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

        product.update(dto.name(), dto.price(), dto.beeType(), dto.content(),
                dto.origin(), dto.transactionType(), dto.transactionMethod());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));

        productRepository.delete(product);
    }
}
