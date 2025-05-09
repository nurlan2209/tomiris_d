package com.diploma.service;

import com.diploma.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;
    
    private final MessageService messageService;
    private final RestTemplate restTemplate;
    
    public GeminiService(MessageService messageService, RestTemplate restTemplate) {
        this.messageService = messageService;
        this.restTemplate = restTemplate;
    }
    
    public List<Map<String, String>> getUserMessages(Long userId) {
        List<Message> messages = messageService.getAllMessageByUserId(userId);
        List<Map<String, String>> chatMessages = new ArrayList<>();
        
        for (Message message : messages) {
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", message.getUserMessage());
            chatMessages.add(userMessage);
            
            Map<String, String> assistantMessage = new HashMap<>();
            assistantMessage.put("role", "assistant");
            assistantMessage.put("content", message.getBotMessage());
            chatMessages.add(assistantMessage);
        }
        
        return chatMessages;
    }
    
    public String askQuestion(Long userId, String question) {
        List<Map<String, String>> history = getUserMessages(userId);
        
        try {
            // URL для Gemini API
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
            
            // Подготовка тела запроса
            Map<String, Object> requestBody = new HashMap<>();
            
            List<Map<String, Object>> contents = new ArrayList<>();
            
            // Если это первое сообщение, добавляем инструкции как часть сообщения пользователя
            if (history.isEmpty()) {
                Map<String, Object> firstContent = new HashMap<>();
                firstContent.put("role", "user");
                
                List<Map<String, String>> parts = new ArrayList<>();
                parts.add(Map.of("text", 
                    "Ты медицинский бот, специализирующийся на диагностике и консультации. " +
                    "Отвечай только на вопросы, связанные с медициной. " +
                    "Всегда указывай, что твои ответы не заменяют консультацию врача. " +
                    "Основывай свои ответы на научных данных. " +
                    "В конце своего ответа укажи возможный диагноз на основе описанных симптомов, " +
                    "отправляй текст без выделения" + "\n\n" +
                    "но предупреди, что для точного диагноза необходимо обратиться к врачу.\n\n" +
                    "Вопрос пользователя: " + question));
                
                firstContent.put("parts", parts);
                contents.add(firstContent);
            } else {
                // Добавляем историю сообщений
                for (Map<String, String> message : history) {
                    Map<String, Object> content = new HashMap<>();
                    content.put("role", message.get("role"));
                    
                    List<Map<String, String>> parts = new ArrayList<>();
                    parts.add(Map.of("text", message.get("content")));
                    content.put("parts", parts);
                    
                    contents.add(content);
                }
                
                // Добавляем текущий вопрос
                Map<String, Object> userContent = new HashMap<>();
                userContent.put("role", "user");
                
                List<Map<String, String>> parts = new ArrayList<>();
                parts.add(Map.of("text", question));
                userContent.put("parts", parts);
                
                contents.add(userContent);
            }
            
            requestBody.put("contents", contents);
            
            // Настройки генерации
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", 0.7);
            generationConfig.put("maxOutputTokens", 1024);
            requestBody.put("generationConfig", generationConfig);
            
            // Выполнение запроса
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                // Парсинг ответа
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                
                if (root.has("candidates") && root.get("candidates").isArray() && root.get("candidates").size() > 0) {
                    JsonNode content = root.get("candidates").get(0).get("content");
                    
                    if (content.has("parts") && content.get("parts").isArray() && content.get("parts").size() > 0) {
                        return content.get("parts").get(0).get("text").asText();
                    }
                }
                
                return "Не удалось получить корректный ответ от Gemini API";
            } else {
                return "Ошибка при обращении к Gemini API: " + response.getStatusCode();
            }
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            return "Произошла ошибка при обращении к Gemini API: " + e.getMessage();
        }
    }
}