package com.example.demo.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping(value = "/logout")
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "home";
    }
}