package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
