package kr.co.webee.presentation.ai.chat.controller;

import kr.co.webee.application.ai.AiGenerationService;
import kr.co.webee.presentation.ai.chat.dto.QuestionRequest;
import kr.co.webee.presentation.ai.chat.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class FaqController {

    private final AiGenerationService aiGenerationService;

    /**
     * 예상 질문 리스트를 반환합니다.
     * 필터: category = 'bee', type = 'faq'
     */
    @GetMapping("/examples")
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
    public ChatResponse answerUserQuestion(@RequestBody QuestionRequest request) {
        return aiGenerationService.answerUserQuestion2(request.question(), request.conversationId());
    }
}
