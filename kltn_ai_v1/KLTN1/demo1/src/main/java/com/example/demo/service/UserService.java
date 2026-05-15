package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.repository.IAccountRepository;
import com.example.demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAccountRepository accountRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean save(User user) {
        return userRepository.save(user) != null;
    }

    @Override
    public boolean update(User user) {
        return userRepository.save(user) != null;
    }

    @Override
    public boolean delete(int id) {
        User user = findById(id);
        if (user == null) return false;
        userRepository.delete(user);
        return true;
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public List<User> search(String keyword) {
        return userRepository.search(keyword);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public User findByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) return null;
        return account.getUser();
    }
}