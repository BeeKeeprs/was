package kr.co.webee.application.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorDocumentResponse {

    private String content;
    private String category;
    private String type;
    private String createdBy;
    private String origin;
    private Double confidence;
    private String createdAt; // ISO-8601
}
