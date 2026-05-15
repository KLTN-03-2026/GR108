package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class ProfileController {

    private final IAccountService accountService;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(IAccountService accountService,
                             IUserService userService,
                             PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // HIỂN THỊ TRANG THÔNG TIN CÁ NHÂN
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        Account account = accountService.findByUsername(principal.getName());

        if (account == null || account.getUser() == null) {
            return "redirect:/login";
        }

        User user = account.getUser();

        model.addAttribute("account", account);
        model.addAttribute("user", user);
        model.addAttribute("student", user.getStudent());

        return "user/profile";
    }

    // CẬP NHẬT THÔNG TIN CÁ NHÂN
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam Integer userId,
                                @RequestParam String fullName,
                                @RequestParam String gender,
                                @RequestParam String email,
                                @RequestParam String phone,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        Account account = accountService.findByUsername(principal.getName());

        if (account == null || account.getUser() == null) {
            return "redirect:/login";
        }

        User currentUser = account.getUser();

        // Chặn user sửa thông tin của người khác
        if (!currentUser.getUserId().equals(userId)) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền cập nhật thông tin này!");
            return "redirect:/users/profile";
        }

        fullName = fullName.trim();
        email = email.trim();
        phone = phone.trim();

        // Kiểm tra email trùng với user khác
        User emailUser = userService.findByEmail(email);
        if (emailUser != null && !emailUser.getUserId().equals(userId)) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng bởi tài khoản khác!");
            return "redirect:/users/profile";
        }

        // Kiểm tra phone trùng với user khác
        User phoneUser = userService.findByPhone(phone);
        if (phoneUser != null && !phoneUser.getUserId().equals(userId)) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã được sử dụng bởi tài khoản khác!");
            return "redirect:/users/profile";
        }

        currentUser.setFullName(fullName);
        currentUser.setGender(gender);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);

        userService.update(currentUser);

        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
        return "redirect:/users/profile";
    }

    // ĐỔI MẬT KHẨU
    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        Account account = accountService.findByUsername(principal.getName());

        if (account == null) {
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "Mật khẩu hiện tại không đúng!");
            return "redirect:/users/profile";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
            return "redirect:/users/profile";
        }

        if (newPassword.trim().length() < 6) {
            redirectAttributes.addFlashAttribute("passwordError", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            return "redirect:/users/profile";
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        accountService.update(account);

        redirectAttributes.addFlashAttribute("passwordSuccess", "Đổi mật khẩu thành công!");
        return "redirect:/users/profile";
    }
}