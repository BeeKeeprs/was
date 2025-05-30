package kr.co.webee.presentation.ai.chat.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.ai.AssistantService;
import kr.co.webee.presentation.ai.chat.api.AssistantApi;
import kr.co.webee.presentation.ai.chat.dto.AssistantRequest;
import kr.co.webee.presentation.ai.chat.dto.AssistantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assistants")
@RequiredArgsConstructor
public class AssistantController implements AssistantApi {

    private final AssistantService assistantService;

    @PostMapping("/messages")
    public AssistantResponse answerUserInput(@RequestBody @Valid AssistantRequest request) {
        return assistantService.answerUserInput(
                request.input(),
                request.conversationId()
        );
    }

    @GetMapping("/sample-questions")
    public List<String> getBeeFaqQuestions() {
        return assistantService.getBeeFaqQuestions();
    }
}
