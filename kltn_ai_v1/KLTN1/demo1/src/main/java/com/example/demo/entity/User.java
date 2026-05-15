package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String fullName;

    private String gender;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String avatar;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Account account;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Student student;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ChatbotLog> chatbotLogs;
}
