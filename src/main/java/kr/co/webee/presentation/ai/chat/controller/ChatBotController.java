package kr.co.webee.presentation.ai.chat.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.ai.AiGenerationService;
import kr.co.webee.presentation.ai.chat.api.ChatBotApi;
import kr.co.webee.presentation.ai.chat.dto.ChatRequest;
import kr.co.webee.presentation.ai.chat.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatBotController implements ChatBotApi {

    private final AiGenerationService aiGenerationService;

    @GetMapping("/examples")
    public List<String> getBeeFaqQuestions() {
        return aiGenerationService.getBeeFaqQuestions();
    }

    @PostMapping("/ask")
    public ChatResponse answerUserQuestion(@RequestBody @Valid ChatRequest request) {
        return aiGenerationService.answerUserQuestion(request.text(), request.conversationId());
    }
}
