package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.emumm.RoleName;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IRoleService;
import com.example.demo.service.IUserService;
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

        if (accountService.findByUsername(username) != null) {
            return "redirect:/register?error=username";
        }

        if (!password.equals(confirmPassword)) {
            return "redirect:/register?error=password";
        }

        User user = new User();
        user.setFullName(fullName);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        userService.save(user);

        Role role = roleService.findByRollName(RoleName.USER);
        if (role == null) {
            role = new Role();
            role.setRollName(RoleName.USER);
            roleService.save(role);
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setUser(user);
        account.setRole(role);

        accountService.save(account);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công!");
        return "redirect:/login";
    }
}