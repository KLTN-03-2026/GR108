package com.example.demo.controller;

import com.example.demo.entity.Room;
import com.example.demo.service.IRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final IRoomService roomService;

    public RoomController(IRoomService roomService) {
        this.roomService = roomService;
    }

    // LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "admin/room/list";
    }

    // CREATE FORM
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("room", new Room());
        return "admin/room/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Room room,
                       @RequestParam(value = "imageFile", required = false) MultipartFile file) throws Exception {

        if (file != null && !file.isEmpty()) {

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.transferTo(new File(uploadDir, fileName));

            room.setImage("/uploads/" + fileName);
        }

        roomService.save(room);
        return "redirect:/rooms";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Room room = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        model.addAttribute("room", room);
        return "admin/room/edit";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        roomService.delete(id);
        return "redirect:/rooms";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Room room = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        model.addAttribute("room", room);
        return "admin/room/detail";
    }
}