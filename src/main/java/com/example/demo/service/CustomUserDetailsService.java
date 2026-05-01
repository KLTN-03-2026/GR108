package com.example.demo.service;

import com.example.demo.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IAccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Account acc = accountService.findByUsername(username);

        if (acc == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        if (acc.getRole() == null || acc.getRole().getRollName() == null) {
            throw new UsernameNotFoundException("User has no role assigned: " + username);
        }

        String role = "ROLE_" + acc.getRole().getRollName().name();
        System.out.println("LOGIN USER: " + username);
        System.out.println("ROLE: " + role);
        return new org.springframework.security.core.userdetails.User(
                acc.getUsername(),
                acc.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}