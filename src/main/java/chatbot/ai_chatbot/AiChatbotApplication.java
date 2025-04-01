package chatbot.ai_chatbot;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import chatbot.ai_chatbot.service.FileService;

@SpringBootApplication
@EntityScan(basePackages = "chatbot.ai_chatbot.model")
public class AiChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiChatbotApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
