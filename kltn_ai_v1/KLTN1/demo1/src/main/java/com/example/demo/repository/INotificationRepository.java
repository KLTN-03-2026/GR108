package com.example.demo.repository;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface INotificationRepository  extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
