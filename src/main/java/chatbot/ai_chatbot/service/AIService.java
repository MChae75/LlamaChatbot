package chatbot.ai_chatbot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIService {
    
    private final RestTemplate restTemplate;
    private String context = ""; // Store extracted text for chatting

    public AIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Step 1: Send extracted text to AI (Llama)
    public String sendFileContentToLlama(String extractedText) {
        if (extractedText == null || extractedText.isBlank()) {
            return "Error: Extracted text is empty.";
        }
        this.context = extractedText;  // Store for future chats
        logger.info("sendFileContentToLlama called with text: {}", extractedText); // Debug log
        return "File content sent to AI for learning.";
    }

    // Step 2: Start Chat - Use extracted text as context
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    public String chatWithLlama(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return "Error: User message is empty.";
        }
        if (context.isBlank()) {
            return "Error: No document has been processed yet. Upload and extract text first.";
        }

        logger.info("chatWithLlama called. Context: {}", context);

        String llamaEndpoint = "http://localhost:11434/api/chat";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", context));
        messages.add(Map.of("role", "user", "content", userMessage));

        requestBody.put("messages", messages);
        requestBody.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            logger.info("Sending request to Llama: {}", requestBody);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(llamaEndpoint, requestEntity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            logger.info("Received response from Llama: {}", responseBody);

            if (responseBody != null && responseBody.containsKey("message")) {
                Map<String, String> message = (Map<String, String>) responseBody.get("message");
                return message.get("content"); // Extract only the AI response
            } else {
                return "Error: AI response format unexpected.";
            }
        } catch (Exception e) {
            logger.error("Error communicating with AI", e);
            return "Error communicating with AI: " + e.getMessage();
        }
    }

    public String summarizeText(String text) {
        String prompt = "Summarize the following document in a short paragraph:\n\n" + text;
        return chatWithLlama(prompt);
    }
    
    public List<String> generateQuestions(String text) {
        String prompt = "Based on this document, generate three interesting questions for discussion without description. Only provide question:\n\n" + text;
        String response = chatWithLlama(prompt);
        
        return List.of(response.split("\n")); // Assuming AI returns questions line by line
    }
}