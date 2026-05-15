package com.example.demo.repository;

import com.example.demo.entity.ChatbotLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IChatbotLogRepository extends JpaRepository<ChatbotLog, Integer> {
    List<ChatbotLog> findByUser_UserIdOrderByCreatedAtDesc(Integer userId);
}
