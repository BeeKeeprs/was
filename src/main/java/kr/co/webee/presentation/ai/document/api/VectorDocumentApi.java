package kr.co.webee.presentation.ai.document.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.presentation.ai.document.dto.VectorDocumentRequest;
import kr.co.webee.presentation.ai.document.dto.VectorDocumentResponse;

import java.util.List;

@Tag(name = "벡터 문서 API (일반적인 사용X)", description = "벡터 저장소 기반 문서 CRUD")
public interface VectorDocumentApi {

    @Operation(summary = "문서 저장", description = "벡터 DB에 새 문서를 저장합니다.")
    String save(VectorDocumentRequest dto, Long userId);

    @Operation(summary = "문서 벌크 저장", description = "여러 개의 문서를 한 번에 저장합니다.")
    String saveBulk(List<VectorDocumentRequest> docs, Long userId);

    @Operation(summary = "문서 검색", description = "입력한 쿼리와 조건(category, type)에 따라 유사 문서를 검색합니다.")
    List<VectorDocumentResponse> search(
            @Parameter(description = "검색어") String query,
            @Parameter(description = "카테고리") String category,
            @Parameter(description = "문서 유형") String type,
            @Parameter(description = "검색 결과 개수") int topK);

    @Operation(summary = "문서 삭제", description = "문서 본문이 정확히 일치하는 문서를 삭제합니다.")
    String delete(@Parameter(description = "삭제할 문서의 본문") String content);

    @Operation(summary = "문서 수정", description = "기존 문서 본문 기준으로 문서를 삭제 후 새로운 문서를 저장합니다.")
    String update(String originalContent, VectorDocumentRequest newDoc, Long userId);
}
