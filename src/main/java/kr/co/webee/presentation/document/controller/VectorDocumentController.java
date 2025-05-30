package kr.co.webee.presentation.document.controller;

import kr.co.webee.application.document.VectorDocumentService;
import kr.co.webee.presentation.document.api.VectorDocumentApi;
import kr.co.webee.presentation.document.dto.VectorDocumentRequest;
import kr.co.webee.presentation.document.dto.VectorDocumentResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
public class VectorDocumentController implements VectorDocumentApi {

    private final VectorDocumentService documentService;

    @PostMapping
    public String save(@RequestBody VectorDocumentRequest dto,
                       @UserId Long userId) {
        documentService.save(dto, userId);
        return "OK";
    }

    @PostMapping("/bulk")
    public String saveBulk(@RequestBody List<VectorDocumentRequest> docs,
                           @UserId Long userId) {
        documentService.saveAll(docs, userId);
        return "OK";
    }

    @GetMapping("/search")
    public List<VectorDocumentResponse> search(@RequestParam String query,
                                               @RequestParam(required = false) String category,
                                               @RequestParam(required = false) String type,
                                               @RequestParam(defaultValue = "5") int topK) {
        return documentService.search(query, category, type, topK);
    }

    @DeleteMapping
    public String delete(@RequestParam String content) {
        documentService.deleteByExactMatch(content);
        return "OK";
    }

    @PutMapping
    public String update(@RequestParam String originalContent,
                         @RequestBody VectorDocumentRequest newDoc,
                         @UserId Long userId) {
        documentService.update(originalContent, newDoc, userId);
        return "OK";
    }
}
