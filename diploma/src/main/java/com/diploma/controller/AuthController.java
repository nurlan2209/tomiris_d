package com.diploma.controller;

import com.diploma.model.request.LoginRequest;
import com.diploma.model.request.RegistrationRequest;
import com.diploma.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationRequest") RegistrationRequest registrationRequest,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            userService.registerUser(registrationRequest);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(required = false) String error,
                                @RequestParam(required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "Неверный логин или пароль.");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы.");
        }

        model.addAttribute("loginRequest", new LoginRequest());

        return "login";
    }
}


