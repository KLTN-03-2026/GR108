package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;

import java.util.List;

public interface INotificationService {
    Notification save(Notification notification);

    List<Notification> findByUser(User user);
    void sendNotificationToAllUsers(String title, String content);
}
