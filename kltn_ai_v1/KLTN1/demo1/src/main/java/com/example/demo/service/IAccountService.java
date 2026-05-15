package com.example.demo.service;

import com.example.demo.dto.AccountDetailDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAccountService {
    Page<Account> findAll(Pageable pageable);
    boolean save(Account account);
    boolean update(Account account);
    boolean delete(int id);
    Account findById(int id);
    Page<AccountDetailDto> searchAll(String name, String username, String email, String phone, Pageable pageable);
    Account findByUsername(String username);
    boolean existsByUsername(String username);
    long count();
    User findUserByUsername(String username);
}
