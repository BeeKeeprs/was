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
public class VectorDocumentRequest {

    private String content;     // 문서 본문
    private String category;    // 예: bee, crop, guide 등
    private String type;        // 예: faq, guide, tip 등
    private String createdBy;   // 작성자 ID 또는 label
    private String origin;      // manual, api, file 등
    private Double confidence;  // 0.0 ~ 1.0
}
