package com.diploma.controller;

import com.diploma.service.GeminiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class GeminiTestController {

    private final GeminiService geminiService;
    
    public GeminiTestController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }
    
    @PostMapping("/gemini")
    public ResponseEntity<String> testGemini(@RequestBody Map<String, String> request) {
        try {
            String question = request.get("question");
            if (question == null || question.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Вопрос не может быть пустым");
            }
            
            // Используем тестовый ID пользователя
            String response = geminiService.askQuestion(1L, question);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ошибка при тестировании Gemini API: " + e.getMessage());
        }
    }
}