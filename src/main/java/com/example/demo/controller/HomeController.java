package com.example.demo.controller;

import com.example.demo.entity.Room;
import com.example.demo.service.IRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    private final IRoomService roomService;

    public HomeController(IRoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String home(Model model) {

        List<Room> rooms = roomService.findAll();

        model.addAttribute("rooms", rooms);

        return "home";
    }
}