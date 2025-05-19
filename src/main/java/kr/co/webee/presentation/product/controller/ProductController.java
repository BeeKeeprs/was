package kr.co.webee.presentation.product.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.product.service.ProductService;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.presentation.annotation.UserId;
import kr.co.webee.presentation.product.api.ProductApi;
import kr.co.webee.presentation.product.dto.request.ProductCreateRequest;
import kr.co.webee.presentation.product.dto.request.ProductUpdateRequest;
import kr.co.webee.presentation.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController implements ProductApi {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Slice<ProductResponse>> getAllProducts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "bee", required = false) BeeType beeType
    ) {
        Slice<ProductResponse> result = productService.getAllProducts(pageable, beeType);
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long createProduct(
            @RequestPart("request") @Valid ProductCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @UserId Long userId
    ) {
        return productService.createProduct(request, images, userId);
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @PutMapping("/{productId}")
    public String updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid ProductUpdateRequest request
    ) {
        // TODO : 이미지 업데이트 로직 추가
        // TODO : 소유자 확인 로직 추가
        productService.updateProduct(productId, request);
        return "OK";
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return "OK";
    }
}
