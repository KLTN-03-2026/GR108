package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {

    Page<User> findAll(Pageable pageable);

    List<User> findAll();

    boolean save(User user);

    boolean update(User user);

    boolean delete(int id);

    User findById(int id);

    User findByEmail(String email);

    User findByPhone(String phone);

    List<User> search(String keyword);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}