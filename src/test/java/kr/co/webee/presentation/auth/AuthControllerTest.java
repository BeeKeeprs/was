package kr.co.webee.presentation.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.webee.application.auth.service.AuthService;
import kr.co.webee.presentation.auth.controller.AuthController;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@WithMockUser
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;


    @Nested
    @DisplayName("회원가입")
    class SignUp{

        @Test
        @DisplayName("성공")
        void signUp_success() throws Exception{
            //given
            SignUpRequest request = SignUpRequest.builder()
                    .username("username")
                    .password("password")
                    .name("name")
                    .build();

            //when
            ResultActions result = mockMvc.perform(post("/api/v1/auth/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .with(csrf()));

            //then
            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isMap())
                    .andExpect(jsonPath("$.data").isEmpty());
        }
    }
}