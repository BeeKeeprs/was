package kr.co.webee.application.ai;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/faq")
@RequiredArgsConstructor
public class FaqController {

    private final AiGenerationService aiGenerationService;

    /**
     * 예상 질문 리스트를 반환합니다.
     * 필터: category = 'bee', type = 'faq'
     */
    @GetMapping("/questions")
    public List<String> getBeeFaqQuestions() {
        return aiGenerationService.getBeeFaqQuestions();
    }

    /**
     * 사용자 질문에 대해 FAQ 벡터 기반 RAG 응답을 반환합니다.
     *
     * @param request 사용자 질문 요청 (JSON: {"question": "..."})
     * @return AI가 생성한 응답 문자열
     */
    @PostMapping("/ask")
    public String answerUserQuestion(@RequestBody QuestionRequest request) {
        return aiGenerationService.answerUserQuestion(request.getQuestion());
    }
}
