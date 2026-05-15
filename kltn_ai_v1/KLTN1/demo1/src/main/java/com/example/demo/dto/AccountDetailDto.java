package com.example.demo.dto;

import com.example.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailDto {
    private String username;
    private String password;
    private String fullName;
    private String gender;
    private String email;
    private String phone;
    private Role role;
}
