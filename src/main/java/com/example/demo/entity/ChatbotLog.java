package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatbot_logs")
@Getter
@Setter
public class ChatbotLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Lob
    private String question;

    @Lob
    private String answer;

    private LocalDateTime createdAt;
}
