package com.diploma.service;

import com.diploma.model.Message;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class GptService {

    private final OpenAiService openAiService;
    private final MessageService messageService;

    public GptService(@Value("${openai.api.key}") String apiKey, MessageService messageService) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
        this.messageService = messageService;
    }

    public List<ChatMessage> getUserMessages(Long userId) {
        List<Message> messages = messageService.getAllMessageByUserId(userId);
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (Message message : messages) {
            chatMessages.add(new ChatMessage("user", message.getUserMessage()));
            chatMessages.add(new ChatMessage("assistant", message.getBotMessage()));
        }
        return chatMessages;
    }

    public String askQuestion(Long userId, String question) {
        List<ChatMessage> history = getUserMessages(userId);
        history.add(new ChatMessage("user", question));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(prependSystemMessage(history))
                .temperature(0.7)
                .maxTokens(1000)
                .build();

        try {
            String response = openAiService.createChatCompletion(chatCompletionRequest)
                    .getChoices().get(0).getMessage().getContent();
            return response;
        } catch (Exception e) {
            System.err.println("Error during OpenAI API call: " + e.getMessage());
            return "Извините, произошла ошибка. Пожалуйста, попробуйте позже.";
        }
    }

    private List<ChatMessage> prependSystemMessage(List<ChatMessage> history) {
        ChatMessage systemMessage = new ChatMessage("system", 
            "Ты медицинский бот, специализирующийся на диагностике и консультации. " +
            "Отвечай только на вопросы, связанные с медициной. " +
            "Всегда указывай, что твои ответы не заменяют консультацию врача. " +
            "Основывай свои ответы на научных данных. " +
            "В конце своего ответа укажи возможный диагноз на основе описанных симптомов, " +
            "но предупреди, что для точного диагноза необходимо обратиться к врачу.");
        history.add(0, systemMessage);
        return history;
    }
}