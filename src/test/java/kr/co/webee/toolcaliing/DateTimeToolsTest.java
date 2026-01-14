package kr.co.webee.toolcaliing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DateTimeToolsTest {

    @Autowired
    ChatModel chatModel;

    @DisplayName("현재 시각을 알 수 있는 tool을 사용해 tool calling을 한다.")
    @Test
    void test(){
        //given

        //when
        String response = ChatClient.create(chatModel)
                .prompt("What day is tomorrow?")
                //.tools(new DateTimeTools())
                .call()
                .content();

        //then
        System.out.println("response = " + response);
    }
}