package chatbot.ai_chatbot.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextExtractor {

    public static String extractTextFromFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile(null, null);
        file.transferTo(tempFile.toFile());
        String content = new String(Files.readAllBytes(tempFile));
        Files.delete(tempFile);
        return content;
    }
}