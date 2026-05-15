package com.example.demo.service;

import com.example.demo.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IAccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("Username is empty");
        }

        Account acc = accountService.findByUsername(username.trim());

        if (acc == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        String roleName = "USER";

        if (acc.getRole() != null && acc.getRole().getRoleName() != null) {
            roleName = acc.getRole().getRoleName().name();
        }

        String authority = "ROLE_" + roleName.toUpperCase();

        System.out.println("LOGIN USER: " + username);
        System.out.println("ROLE: " + authority);
        System.out.println("PASSWORD DB: " + acc.getPassword());

        return new org.springframework.security.core.userdetails.User(
                acc.getUsername(),
                acc.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(authority))
        );
    }
}