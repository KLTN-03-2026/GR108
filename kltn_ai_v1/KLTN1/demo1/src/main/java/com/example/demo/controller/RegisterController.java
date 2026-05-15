package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.emumm.RoleName;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IRoleService;
import com.example.demo.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final IUserService userService;
    private final IAccountService accountService;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String showRegister() {
        return "register";
    }

    @PostMapping
    @Transactional
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String fullName,
            @RequestParam String gender,
            @RequestParam String email,
            @RequestParam String phone,
            RedirectAttributes redirectAttributes
    ) {

        username = username.trim();
        fullName = fullName.trim();
        email = email.trim();
        phone = phone.trim();

        if (accountService.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "redirect:/register";
        }

        if (userService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
            return "redirect:/register";
        }

        if (userService.existsByPhone(phone)) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã tồn tại!");
            return "redirect:/register";
        }

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu nhập lại không khớp!");
            return "redirect:/register";
        }

        User user = new User();
        user.setFullName(fullName);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        userService.save(user);

        Role role = roleService.findByRoleName(RoleName.USER);

        if (role == null) {
            role = new Role();
            role.setRoleName(RoleName.USER);
            roleService.save(role);
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setUser(user);
        account.setRole(role);

        accountService.save(account);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }
}