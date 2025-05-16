//package kr.co.webee.application.ai;
//
//import kr.co.webee.annotation.IntegrationTest;
//import kr.co.webee.presentation.ai.chat.dto.ChatResponse;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@IntegrationTest
//class AiGenerationServiceTest {
//
//    @Autowired
//    AiGenerationService aiGenerationService;
//
//    @Test
//    void answerUserQuestion2() {
//
//        String question1 = "전 홍길동입니다";
//        String question2 = "제 이름은 무엇일까요?";
//        String conversationId = "test-conversation-id";
//
//        // when
//        ChatResponse res1 = aiGenerationService.answerUserQuestion2(question1, conversationId);
//        ChatResponse res2 = aiGenerationService.answerUserQuestion2(question2, conversationId);
//
//        System.out.println(res1);
//        System.out.println(res2);
//
//    }
//
//}
