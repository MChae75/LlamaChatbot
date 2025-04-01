package chatbot.ai_chatbot.controller;

import chatbot.ai_chatbot.service.FileService;
import chatbot.ai_chatbot.utils.TextExtractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import java.io.IOException;
import java.io.InputStream;
import chatbot.ai_chatbot.service.AIService;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private AIService aiService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String message = fileService.uploadFile(file);
            return ResponseEntity.ok(message);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/extract-text")
    public ResponseEntity<String> extractText(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Tika tika = new Tika();
            String extractedText = tika.parseToString(inputStream);

            aiService.sendFileContentToLlama(extractedText); // Send to Llama without storing response

            return ResponseEntity.ok(extractedText);
        } catch (IOException | TikaException e) {
            return ResponseEntity.status(500).body("Error extracting text: " + e.getMessage());
        }
    }

    @PostMapping("/summarize")
    public ResponseEntity<String> summarizeExtractedText(@RequestBody String extractedText) {     
        String summary = aiService.summarizeText(extractedText);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/generate-questions")
    public ResponseEntity<List<String>> generateQuestions(@RequestBody String extractedText) {
        List<String> questions = aiService.generateQuestions(extractedText);
        return ResponseEntity.ok(questions);
    }
}