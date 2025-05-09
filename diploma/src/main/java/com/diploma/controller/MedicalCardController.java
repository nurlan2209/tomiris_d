package com.diploma.controller;

import com.diploma.model.MedicalCard;
import com.diploma.model.User;
import com.diploma.model.request.MedicalCardRequest;
import com.diploma.service.MedicalCardService;
import com.diploma.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/medical-card")
public class MedicalCardController {

    private final MedicalCardService medicalCardService;
    private final UserService userService;

    public MedicalCardController(MedicalCardService medicalCardService, UserService userService) {
        this.medicalCardService = medicalCardService;
        this.userService = userService;
    }

    @GetMapping
    public String showMedicalCard(Model model, Authentication authentication) {
        Long userId = getUserId(authentication);
        MedicalCard medicalCard = medicalCardService.getMedicalCardByUserId(userId);
        
        MedicalCardRequest medicalCardRequest = new MedicalCardRequest();
        if (medicalCard.getId() != null) {
            medicalCardRequest.setBirthDate(medicalCard.getBirthDate());
            medicalCardRequest.setGender(medicalCard.getGender());
            medicalCardRequest.setHeight(medicalCard.getHeight());
            medicalCardRequest.setWeight(medicalCard.getWeight());
            medicalCardRequest.setBloodType(medicalCard.getBloodType());
            medicalCardRequest.setAllergies(medicalCard.getAllergies());
            medicalCardRequest.setChronicDiseases(medicalCard.getChronicDiseases());
            medicalCardRequest.setCurrentMedications(medicalCard.getCurrentMedications());
            medicalCardRequest.setMedicalHistory(medicalCard.getMedicalHistory());
        }
        
        model.addAttribute("medicalCard", medicalCard);
        model.addAttribute("medicalCardRequest", medicalCardRequest);
        model.addAttribute("authentication", authentication);
        model.addAttribute("userName", userService.getUserById(userId).getName());
        
        return "medical-card";
    }

    @PostMapping
    public String updateMedicalCard(
            @Valid @ModelAttribute("medicalCardRequest") MedicalCardRequest medicalCardRequest,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authentication", authentication);
            model.addAttribute("userName", userService.getUserById(getUserId(authentication)).getName());
            return "medical-card";
        }

        Long userId = getUserId(authentication);
        MedicalCard medicalCard = medicalCardService.createOrUpdateMedicalCard(userId, medicalCardRequest);
        
        model.addAttribute("medicalCard", medicalCard);
        model.addAttribute("success", "Медицинская карта успешно обновлена");
        model.addAttribute("authentication", authentication);
        model.addAttribute("userName", userService.getUserById(userId).getName());
        
        return "medical-card";
    }
    
    @GetMapping("/bmi-calculator")
    public String showBmiCalculator(Model model, Authentication authentication) {
        Long userId = getUserId(authentication);
        MedicalCard medicalCard = medicalCardService.getMedicalCardByUserId(userId);
        
        model.addAttribute("medicalCard", medicalCard);
        model.addAttribute("authentication", authentication);
        model.addAttribute("userName", userService.getUserById(userId).getName());
        
        return "bmi-calculator";
    }
    
    @PostMapping("/calculate-bmi")
    public String calculateBMI(
            @RequestParam("height") Integer height,
            @RequestParam("weight") Integer weight,
            Authentication authentication,
            Model model) {
        
        Long userId = getUserId(authentication);
        MedicalCard medicalCard = medicalCardService.getMedicalCardByUserId(userId);
        
        // Временно установим значения для расчета
        medicalCard.setHeight(height);
        medicalCard.setWeight(weight);
        
        Double bmi = medicalCard.calculateBMI();
        String category = medicalCard.getBMICategory();
        
        model.addAttribute("medicalCard", medicalCard);
        model.addAttribute("bmi", bmi);
        model.addAttribute("bmiCategory", category);
        model.addAttribute("height", height);
        model.addAttribute("weight", weight);
        model.addAttribute("authentication", authentication);
        model.addAttribute("userName", userService.getUserById(userId).getName());
        
        return "bmi-calculator";
    }
    
    @GetMapping("/diagnosis")
    public String showDiagnosisForm(Model model, Authentication authentication) {
        Long userId = getUserId(authentication);
        MedicalCard medicalCard = medicalCardService.getMedicalCardByUserId(userId);
        
        model.addAttribute("medicalCard", medicalCard);
        model.addAttribute("authentication", authentication);
        model.addAttribute("userName", userService.getUserById(userId).getName());
        
        return "diagnosis-form";
    }
    
    @PostMapping("/diagnosis")
    public String getDiagnosis(
            @RequestParam("symptoms") String symptoms,
            Authentication authentication,
            Model model) {
            
        Long userId = getUserId(authentication);
        MedicalCard medicalCard = medicalCardService.getMedicalCardByUserId(userId);
        
        String diagnosis = medicalCardService.getPreliminaryDiagnosis(medicalCard, symptoms);
        
        model.addAttribute("medicalCard", medicalCard);
        model.addAttribute("symptoms", symptoms);
        model.addAttribute("diagnosis", diagnosis);
        model.addAttribute("authentication", authentication);
        model.addAttribute("userName", userService.getUserById(userId).getName());
        
        return "diagnosis-form";
    }

    private Long getUserId(Authentication authentication) {
        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        return user.getId();
    }
}