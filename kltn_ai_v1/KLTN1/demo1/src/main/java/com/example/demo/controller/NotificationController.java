package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.INotificationService;
import com.example.demo.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationController {

    private final INotificationService notificationService;
    private final IUserService userService;

    public NotificationController(
            INotificationService notificationService,
            IUserService userService
    ) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/notifications")
    public String notifications(
            Authentication authentication,
            Model model
    ) {

        User user = userService.findByUsername(
                authentication.getName()
        );

        model.addAttribute(
                "notifications",
                notificationService.findByUser(user)
        );

        return "guest/notification/list";
    }
    @GetMapping("/admin/send-test")
    public String sendTestNotification() {

        notificationService.sendNotificationToAllUsers(
                "Thông báo hệ thống",
                "Admin vừa gửi thông báo cho tất cả user"
        );

        return "redirect:/notifications";
    }
}