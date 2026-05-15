package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ) {

        Page<User> userPage =
                userService.findAll(PageRequest.of(page, size));

        model.addAttribute("users", userPage.getContent());

        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages", userPage.getTotalPages());

        return "admin/user/list";
    }
    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("user", new User());

        return "admin/user/add";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute User user,
                       Model model) {

        if (userService.existsByEmail(user.getEmail())) {

            model.addAttribute("error",
                    "Email already exists");

            model.addAttribute("user", user);

            return "admin/user/add";
        }

        if (userService.existsByPhone(user.getPhone())) {

            model.addAttribute("error",
                    "Phone already exists");

            model.addAttribute("user", user);

            return "admin/user/add";
        }

        userService.save(user);

        return "redirect:/users";
    }

    // ===================== EDIT FORM =====================

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id,
                       Model model) {

        User user = userService.findById(id);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        model.addAttribute("user", user);

        return "admin/user/edit";
    }

    // ===================== UPDATE =====================

    @PostMapping("/update")
    public String update(@ModelAttribute User user,
                         Model model) {

        User oldUser = userService.findById(user.getUserId());

        if (oldUser == null) {
            throw new RuntimeException("User not found");
        }

        // kiểm tra email trùng
        User emailUser =
                userService.findByEmail(user.getEmail());

        if (emailUser != null &&
                !emailUser.getUserId().equals(user.getUserId())) {

            model.addAttribute("error",
                    "Email already exists");

            model.addAttribute("user", user);

            return "admin/user/edit";
        }

        // kiểm tra phone trùng
        User phoneUser =
                userService.findByPhone(user.getPhone());

        if (phoneUser != null &&
                !phoneUser.getUserId().equals(user.getUserId())) {

            model.addAttribute("error",
                    "Phone already exists");

            model.addAttribute("user", user);

            return "admin/user/edit";
        }

        userService.update(user);

        return "redirect:/users";
    }

    // ===================== DELETE =====================

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        userService.delete(id);

        return "redirect:/users";
    }

    // ===================== DETAIL =====================

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id,
                         Model model) {

        User user = userService.findById(id);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        model.addAttribute("user", user);

        return "admin/user/detail";
    }

    // ===================== SEARCH =====================

    @GetMapping("/search")
    public String search(@RequestParam String keyword,
                         Model model) {

        model.addAttribute("users",
                userService.search(keyword));

        return "admin/user/list";
    }
}