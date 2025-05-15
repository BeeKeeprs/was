//package kr.co.webee.application.ai;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.ai.chat.client.ChatClientRequest;
//import org.springframework.ai.chat.client.ChatClientResponse;
//import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
//import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
//import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
//import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.core.Ordered;
//
//import reactor.core.publisher.Flux;
//
//public class ReReadingAdvisor implements CallAdvisor, StreamAdvisor {
//
//    private ChatClientRequest enhanceRequest(ChatClientRequest request) {
//        Prompt originalPrompt = request.prompt();
//        List<UserMessage> originalMessages = originalPrompt.getUserMessages();
//
//        // 마지막 사용자 메시지에 대해 리프레이즈 처리
//        String lastUserText = originalMessages.isEmpty() ? "" : originalMessages.get(originalMessages.size() - 1)
//        		.getText();
//
//        String enhanced = String.format("""
//            %s
//            다시 한번 이 문장을 찬찬히 읽어봐.: %s
//            """, lastUserText, lastUserText);
//
//        UserMessage newUserMessage = new UserMessage(enhanced);
//        Prompt modifiedPrompt = new Prompt(List.of(newUserMessage));
//
//        Map<String, Object> newContext = new HashMap<>(request.context());
//        newContext.put("re2_input_query", lastUserText);
//
//        return new ChatClientRequest(modifiedPrompt, newContext);
//    }
//
//    @Override
//    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
//        return chain.nextCall(enhanceRequest(request));
//    }
//
//    @Override
//    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
//        return chain.nextStream(enhanceRequest(request));
//    }
//
//    @Override
//    public String getName() {
//        return "reReadingAdvisor";
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
//}
