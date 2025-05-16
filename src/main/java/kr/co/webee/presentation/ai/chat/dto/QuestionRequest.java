package kr.co.webee.presentation.ai.chat.dto;

public record QuestionRequest(
        String question,
        String conversationId
) {
}
