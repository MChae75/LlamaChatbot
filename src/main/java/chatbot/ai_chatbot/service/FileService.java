package chatbot.ai_chatbot.service;

import chatbot.ai_chatbot.utils.TextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


@Service
public class FileService {

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/uploads/";


    @Autowired
    private AIService aiService;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = Paths.get(UPLOAD_DIR, fileName).toString();
    
        // Save file to disk
        file.transferTo(new File(filePath));
    
        return "File uploaded successfully: " + filePath;
    }

    public String extractText(MultipartFile file) throws IOException {
        String extractedText = TextExtractor.extractTextFromFile(file);

        // Send extracted text to AI (Llama)
        aiService.sendFileContentToLlama(extractedText);

        return "Text extracted and sent to AI.";
    }
}