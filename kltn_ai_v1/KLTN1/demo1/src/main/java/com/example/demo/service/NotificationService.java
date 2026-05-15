package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.INotificationRepository;
import com.example.demo.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService implements INotificationService {

    private final INotificationRepository notificationRepository;

    private final IUserRepository userRepository;

    public NotificationService(INotificationRepository notificationRepository1,
                               IUserRepository userRepository) {

        this.notificationRepository = notificationRepository1;

        this.userRepository = userRepository;
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findByUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public void sendNotificationToAllUsers(String title, String content) {

        List<User> users = userRepository.findAll();

        for (User user : users) {

            Notification notification = new Notification();

            notification.setTitle(title);

            notification.setContent(content);

            notification.setUser(user);

            notification.setCreatedAt(LocalDateTime.now());

            notification.setRead(false);

            notificationRepository.save(notification);
        }
    }
}