package chatbot.ai_chatbot.controller;

import chatbot.ai_chatbot.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private AIService aiService;

    @PostMapping("/query")
    public ResponseEntity<String> chat(@RequestBody String userMessage) {
        String response = aiService.chatWithLlama(userMessage);
        return ResponseEntity.ok(response);
    }
}