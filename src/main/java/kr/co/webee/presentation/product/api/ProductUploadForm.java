package kr.co.webee.presentation.product.api;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 상품 업로드 폼
 * <p>
 * 상품 JSON 데이터와 이미지 파일들을 함께 전송하기 위한 폼 클래스입니다.
 * Swagger 문서화에만 사용하고 실제 서비스 로직이나 비즈니스 로직에과는 무관합니다.
 */
@Schema(description = "상품 업로드 Swagger 폼")
public class ProductUploadForm {

    @Schema(
            description = "상품 JSON 데이터",
            type = "string",
            format = "binary",
            required = true
    )
    private String request;

    @ArraySchema(schema = @Schema(type = "string", format = "binary"))
    @Schema(description = "이미지 파일들", required = true)
    private List<MultipartFile> images;
}
