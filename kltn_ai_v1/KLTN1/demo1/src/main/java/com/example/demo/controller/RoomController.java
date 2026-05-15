package com.example.demo.controller;

import com.example.demo.emumm.RoomGender;
import com.example.demo.entity.Room;
import com.example.demo.service.IRoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final IRoomService roomService;

    public RoomController(IRoomService roomService) {
        this.roomService = roomService;
    }
    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) RoomGender gender,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {

        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }

        if (status != null && status.trim().isEmpty()) {
            status = null;
        }

        Pageable pageable = PageRequest.of(page, 7);

        Page<Room> rooms = roomService.search(
                keyword,
                minPrice,
                maxPrice,
                gender,
                status,
                pageable
        );

        model.addAttribute("rooms", rooms);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("gender", gender);
        model.addAttribute("status", status);

        return "admin/room/list";
    }
    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("room", new Room());
        model.addAttribute("genders", RoomGender.values());

        return "admin/room/add";
    }

    @PostMapping("/save")
    public String save(

            @ModelAttribute Room room,
            @RequestParam(value = "imageFile", required = false)
            MultipartFile file

    ) throws Exception {

        if (file != null && !file.isEmpty()) {

            String fileName =
                    System.currentTimeMillis() + "_" + file.getOriginalFilename();

            String uploadDir =
                    System.getProperty("user.dir") + "/uploads/";

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
        model.addAttribute("genders", RoomGender.values());

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