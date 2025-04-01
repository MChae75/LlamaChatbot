package chatbot.ai_chatbot.controller;

import chatbot.ai_chatbot.service.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody String userInput) {
        String response = aiService.sendFileContentToLlama(userInput);
        return ResponseEntity.ok(response);
    }
}