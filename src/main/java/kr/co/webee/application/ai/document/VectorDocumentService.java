package kr.co.webee.application.ai.document;

import kr.co.webee.presentation.ai.document.dto.VectorDocumentRequest;
import kr.co.webee.presentation.ai.document.dto.VectorDocumentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class VectorDocumentService {

    private final VectorStore vectorStore;

    @Async("uploadExecutor")
    public CompletableFuture<Void> save(VectorDocumentRequest request, Long userId) {
        String createdBy = String.valueOf(userId);
        try {
            Document doc = toDocument(request, createdBy);
            vectorStore.add(List.of(doc));
        } catch (Exception e) {
            log.error("문서[{}] 업로드 실패: {}", request.content(), e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public void saveAll(List<VectorDocumentRequest> dtos, Long userId) {
        String createdBy = String.valueOf(userId);
        List<Document> docs = dtos.stream()
                .map(dto -> toDocument(dto, createdBy))
                .toList();
        vectorStore.add(docs);
    }

    public List<VectorDocumentResponse> search(String query, String category, String type, int topK) {
        String filterExpr = Stream.of(
                category != null ? "category == '" + category + "'" : null,
                type != null ? "type == '" + type + "'" : null
        ).filter(Objects::nonNull).collect(Collectors.joining(" AND "));

        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .filterExpression(filterExpr.isBlank() ? null : filterExpr)
                .build();

        List<Document> docs = vectorStore.similaritySearch(request);

        return Stream.ofNullable(docs)
                .flatMap(List::stream)
                .map(this::toResponse).toList();
    }

    public void deleteByExactMatch(String content) {
        List<Document> matches = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(content)
                        .topK(10)
                        .build()
        );

        Stream.ofNullable(matches)
                .flatMap(List::stream)
                .filter(d -> content.equals(d.getText()))
                .forEach(d -> vectorStore.delete(d.getId()));
    }

    public void update(String originalContent, VectorDocumentRequest newDoc, Long userId) {
        deleteByExactMatch(originalContent);
        save(newDoc, userId);
    }

    public Document toDocument(VectorDocumentRequest req, String createdBy) {
        Map<String, Object> metadata = Map.of(
                "category", req.category(),
                "type", req.type(),
                "created_by", createdBy,
                "origin", req.origin(),
                "confidence", req.confidence(),
                "created_at", LocalDateTime.now().toString()
        );
        return new Document(req.content(), metadata);
    }

    public VectorDocumentResponse toResponse(Document doc) {
        Map<String, Object> m = doc.getMetadata();
        return VectorDocumentResponse.builder()
                .content(doc.getText())
                .category((String) m.get("category"))
                .type((String) m.get("type"))
                .createdBy((String) m.get("created_by"))
                .origin((String) m.get("origin"))
                .confidence(Double.valueOf(m.get("confidence").toString()))
                .createdAt((String) m.get("created_at"))
                .build();
    }
}
