package com.example.demo.controller;

import com.example.demo.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @Autowired
    private IRoomService roomService;

    @GetMapping("/admin/home")
    public String home(Model model) {
        model.addAttribute("roomCount", roomService.findAll().size());
        return "admin/home-admin";
    }
}