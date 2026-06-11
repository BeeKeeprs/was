package kr.co.webee.infrastructure.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Profile("!test")
@Configuration
public class FcmConfig {
    @Value("${firebase.key-path}")
    private String firebaseKeyPath;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        InputStream serviceAccount = new ClassPathResource(firebaseKeyPath).getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
