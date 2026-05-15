package com.example.demo.controller;

import com.example.demo.emumm.RoleName;
import com.example.demo.entity.Account;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IRoleService;
import com.example.demo.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller

public class AccountController {

    private final IUserService userService;
    private final IAccountService accountService;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AccountController(IUserService userService,
                             IAccountService accountService,
                             IRoleService roleService,
                             PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.accountService = accountService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/account/forgot-password")
    public String showForgotPasswordForm() {
        return "guest/forgot-password";
    }

    @GetMapping("/admin/accounts")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/account/list";
    }

    @GetMapping("/admin/accounts/create")
    public String createAccount() {
        return "admin/account/add";
    }

    @PostMapping("/admin/accounts/save")
    public String saveAccount(@RequestParam String fullName,
                              @RequestParam String gender,
                              @RequestParam String email,
                              @RequestParam String phone,
                              @RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String roleName) {

        if (accountService.findByUsername(username) != null) {
            return "redirect:/admin/accounts/create?error=username";
        }

        if (userService.existsByEmail(email)) {
            return "redirect:/admin/accounts/create?error=email";
        }

        if (userService.existsByPhone(phone)) {
            return "redirect:/admin/accounts/create?error=phone";
        }

        User user = new User();
        user.setFullName(fullName);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        userService.save(user);

        RoleName roleEnum = RoleName.valueOf(roleName);

        Role role = roleService.findByRoleName(roleEnum);

        if (role == null) {
            return "redirect:/admin/accounts/create?error=role_not_found";
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setUser(user);
        account.setRole(role);

        accountService.save(account);

        return "redirect:/admin/accounts";
    }
    @GetMapping("/admin/accounts/delete/{id}")
    public String deleteAccount(@PathVariable Integer id) {
        accountService.delete(id);
        return "redirect:/admin/accounts";
    }
}