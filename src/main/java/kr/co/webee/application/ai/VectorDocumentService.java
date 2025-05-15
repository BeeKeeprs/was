package kr.co.webee.application.ai;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VectorDocumentService {

    private final VectorStore vectorStore;

    public void save(VectorDocumentRequest dto) {
        Document doc = toDocument(dto);
        vectorStore.add(List.of(doc));
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
        return docs.stream().map(this::toResponse).toList();
    }

    public void deleteByExactMatch(String content) {
        List<Document> matches = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(content)
                .topK(10)
                .build()
        );
        matches.stream()
            .filter(d -> content.equals(d.getText()))
            .forEach(d -> vectorStore.delete(d.getId()));
    }

    public void update(String originalContent, VectorDocumentRequest newDoc) {
        deleteByExactMatch(originalContent);
        save(newDoc);
    }

    public Document toDocument(VectorDocumentRequest req) {
        Map<String, Object> metadata = Map.of(
            "category", req.getCategory(),
            "type", req.getType(),
            "created_by", req.getCreatedBy(),
            "origin", req.getOrigin(),
            "confidence", req.getConfidence(),
            "created_at", LocalDateTime.now().toString()
        );
        return new Document(req.getContent(), metadata);
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
