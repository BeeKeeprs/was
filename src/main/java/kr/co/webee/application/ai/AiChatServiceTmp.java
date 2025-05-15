//package kr.co.webee.application.ai;
//
//package kr.co.webee.application.ai;
//
//import java.util.Map;
//import java.util.function.Consumer;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.ChatClient.AdvisorSpec;
//import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.chat.prompt.PromptTemplate;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//import org.springframework.util.MimeType;
//
//import kr.co.webee.application.ai.tool.DateTimeTools;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AiChatService {
//
//	private final ChatClient simpleChatClient;
//	private final ChatClient advisedChatClient;
//	private final ChatClient ragChatDefaultClient;
//	private final ChatClient ragChatCustomClient;
////	private final MemberTools memberTools;
//	private final DateTimeTools dateTimeTools;
//
////	public Object simpleGeneration(String userInput) {
////		return simpleChatClient.prompt()
////			.system(s -> s.param("language", "korean").param("character", "chill"))
////			.user(userInput)
////			.call()
////			.content();
////	}
////
////	public Quiz quiz(String category) {
////		PromptTemplate pt = new PromptTemplate("""
////			{category}와 관련된 재밌는 퀴즈를 내줘.
////			퀴즈는 내용(question), 정답(answer), 이유(reason)이 필요해.
////		""");
////
////		Prompt prompt = pt.create(Map.of("category", category));
////
////		return simpleChatClient.prompt(prompt)
////			.system(s -> s.param("language", "korean").param("character", "chill"))
////			.call()
////			.entity(new ParameterizedTypeReference<Quiz>() {});
////	}
//
//	public String multiModal(String userInput, MimeType mime, Resource resource) {
//		return simpleChatClient.prompt()
//			.system(s -> s.param("language", "korean").param("character", "chill"))
//			.user(u -> u.text(userInput).media(mime, resource))
//			.call()
//			.content();
//	}
//
//	public String advisedGeneration(String userInput) {
//		return advisedChatClient.prompt()
//			.system(s -> s.param("language", "Korean").param("character", "Chill한"))
//			.user(userInput)
//			.call()
//			.content();
//	}
//
//	public String timeToolGeneration(String userInput) {
//		return advisedChatClient.prompt()
//			.system(s -> s.param("language", "Korean").param("character", "Chill한"))
//			.user(userInput)
//			.tools(dateTimeTools)
//			.call()
//			.content();
//	}
//
////	public String memberToolGeneration(String userInput) {
////		return advisedChatClient.prompt()
////			.system(s -> s.param("language", "Korean").param("character", "Chill한"))
////			.user(userInput)
////			.tools(memberTools)
////			.call()
////			.content();
////	}
//
//	public String ragGeneration(String userInput, boolean contextOnly, Consumer<AdvisorSpec> advisorSpec) {
//		ChatClient chatClient = contextOnly ? ragChatDefaultClient : ragChatCustomClient;
//
//		ChatClientRequestSpec promptSpec = chatClient.prompt()
//			.system(s -> s.param("language", "Korean").param("character", "Chill한"))
//			.user(userInput);
//
//		if (advisorSpec != null) {
//			promptSpec.advisors(advisorSpec);
//		}
//
//		return promptSpec.call().content();
//	}
//}
