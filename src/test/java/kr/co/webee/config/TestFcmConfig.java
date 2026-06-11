package kr.co.webee.config;

import com.google.firebase.FirebaseApp;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestFcmConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        return Mockito.mock(FirebaseApp.class);
    }
}
