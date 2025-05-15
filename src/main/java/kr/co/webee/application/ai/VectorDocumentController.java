package kr.co.webee.application.ai;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "벡터 문서 API", description = "벡터 저장소 기반 문서 CRUD")
public class VectorDocumentController {

    private final VectorDocumentService documentService;

    @Operation(summary = "문서 저장", description = "벡터 DB에 새 문서를 저장합니다.")
    @PostMapping
    public String save(@RequestBody VectorDocumentRequest dto) {
        documentService.save(dto);
        return "OK";
    }

    @Operation(summary = "문서 검색", description = "입력한 쿼리와 조건(category, type)에 따라 유사 문서를 검색합니다.")
    @GetMapping("/search")
    public List<VectorDocumentResponse> search(
            @Parameter(description = "검색어 (문서 본문 기준)")
            @RequestParam("query") String query,

            @Parameter(description = "카테고리 (예: bee, crop)")
            @RequestParam(name = "category", required = false) String category,

            @Parameter(description = "문서 유형 (예: faq, guide)")
            @RequestParam(name = "type", required = false) String type,

            @Parameter(description = "검색 결과 개수")
            @RequestParam(name = "topK", defaultValue = "5") int topK) {
        return documentService.search(query, category, type, topK);
    }

    @Operation(summary = "문서 삭제", description = "문서 본문이 정확히 일치하는 문서를 삭제합니다.")
    @DeleteMapping
    public String delete(@Parameter(description = "삭제할 문서의 본문 내용") @RequestParam String content) {
        documentService.deleteByExactMatch(content);
        return "OK";
    }

    @Operation(summary = "문서 수정", description = "기존 문서 본문 기준으로 문서를 삭제 후 새로운 문서를 저장합니다.")
    @PutMapping
    public String update(
            @Parameter(description = "기존 문서의 본문 내용") @RequestParam String originalContent,
            @RequestBody VectorDocumentRequest newDoc) {
        documentService.update(originalContent, newDoc);
        return "OK";
    }
}
