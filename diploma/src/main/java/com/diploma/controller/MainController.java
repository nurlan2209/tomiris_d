package com.diploma.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("authentication", authentication);
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model, Authentication authentication) {
        model.addAttribute("authentication", authentication); 
        return "about";
    }

    @GetMapping("/diseases")
    public String diseases(Model model, Authentication authentication) {
        model.addAttribute("authentication", authentication); 
        return "diseases";
    }

    @GetMapping("/more-details1")
    public String moreDetails1(Model model) {
        return "more-details1";
    }

    @GetMapping("/more-details2")
    public String moreDetails2(Model model) {
        return "more-details2";
    }

    @GetMapping("/more-details3")
    public String moreDetails3(Model model) {
        return "more-details3";
    }

    @GetMapping("/more-details4")
    public String moreDetails4(Model model) {
        return "more-details4";
    }

    @GetMapping("/more-details5")
    public String moreDetails5(Model model) {
        return "more-details5";
    }

    @GetMapping("/more-details6")
    public String moreDetails6(Model model) {
        return "more-details6";
    }

}