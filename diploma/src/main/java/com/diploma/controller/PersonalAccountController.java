package com.diploma.controller;

import com.diploma.model.User;
import com.diploma.service.GeminiService;
import com.diploma.service.MessageService;
import com.diploma.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PersonalAccountController {

    private final GeminiService geminiService; // Изменено с GptService на GeminiService
    private final UserService userService;
    private final MessageService messageService;

    public PersonalAccountController(GeminiService geminiService, UserService userService, MessageService messageService) {
        this.geminiService = geminiService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        model.addAttribute("authentication", authentication);
        model.addAttribute("messages", geminiService.getUserMessages(getUserId(authentication)));
        model.addAttribute("userName", userService.getUserById(getUserId(authentication)).getName());
        return "cabinet";
    }

    @PostMapping("/ask-question")
    public String askQuestion(@RequestParam("question") String question,
                              Authentication authentication,
                              Model model) {
        Long userId = getUserId(authentication);
        String response = geminiService.askQuestion(userId, question);
        messageService.createMessage(userId, question, response);

        model.addAttribute("userName", userService.getUserById(userId).getName());
        model.addAttribute("authentication", authentication);
        model.addAttribute("messages", geminiService.getUserMessages(userId));
        model.addAttribute("question", question);
        model.addAttribute("response", response);

        return "cabinet";
    }

    private Long getUserId(Authentication authentication) {
        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        return user.getId();
    }
}