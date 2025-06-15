package kr.co.webee.presentation.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.presentation.product.dto.request.ProductCreateRequest;
import kr.co.webee.presentation.product.dto.request.ProductUpdateRequest;
import kr.co.webee.presentation.product.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Product", description = "상품 관련 API")
public interface ProductApi {

    @Operation(summary = "전체 상품 조회 (페이징)", description = "전체 상품 목록을 페이지 단위로 조회합니다.")
    @GetMapping
    ResponseEntity<?> getAllProducts(
            Pageable pageable, BeeType beeType, Long businessId
    );

    @Operation(
            summary = "상품 등록",
            description = "상품 생성 요청(JSON)과 이미지 파일들을 multipart/form-data로 함께 전송합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Long createProduct(
            @Parameter(
                    description = "상품 생성 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductCreateRequest.class)
                    )
            )
            @RequestPart("request") ProductCreateRequest request,

            @Parameter(
                    description = "이미지 파일들 (선택)",
                    array = @ArraySchema(
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    );

    @Operation(
            summary = "상품 조회",
            description = "상품 ID에 해당하는 상품 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "상품 없음")
    })
    ProductResponse getProduct(
            @Parameter(description = "조회할 상품 ID", example = "1", required = true)
            Long productId
    );

    @Operation(
            summary = "상품 수정",
            description = "상품 ID에 해당하는 상품 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "해당 상품 없음")
    })
    String updateProduct(
            @Parameter(description = "상품 ID", example = "1")
            Long productId,

            @Parameter(description = "수정할 상품 정보(JSON)", required = true)
            ProductUpdateRequest request
    );

    @Operation(
            summary = "상품 삭제",
            description = "상품 ID에 해당하는 상품을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 상품 없음")
    })
    String deleteProduct(
            @Parameter(description = "상품 ID", example = "1")
            Long productId
    );
}
