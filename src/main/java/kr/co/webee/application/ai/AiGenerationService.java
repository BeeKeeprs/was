package kr.co.webee.application.ai;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import kr.co.webee.infrastructure.config.ai.AiGenerationClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiGenerationService {

	private final AiGenerationClient aiGenerationClient;
	private final VectorStore vectorStore;
	
	public List<String> getBeeFaqQuestions() {
	    SearchRequest searchRequest = SearchRequest.builder()
	            .query("bee faq")
	            .topK(3)
	            .filterExpression("type == 'faq' AND category == 'bee'")
	            .build();

	    List<Document> faqDocs = vectorStore.similaritySearch(searchRequest);
	    return faqDocs.stream()
	            .map(Document::getText) // content는 text로 저장됨
	            .toList();
	}

	public String answerUserQuestion(String userQuestion) {
        return aiGenerationClient.ragGeneration(
            userQuestion,
            false, // contextOnly: false → 커스텀 context + 벡터 + AI 사용
            Map.of(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type != 'faq' AND category == 'bee'")
        );
    }
}
