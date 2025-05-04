package kr.co.webee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class WebeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebeeApplication.class, args);
	}

}
