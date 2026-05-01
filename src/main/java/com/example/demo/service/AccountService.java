package com.example.demo.service;

import com.example.demo.dto.AccountDetailDto;
import com.example.demo.entity.Account;
import com.example.demo.repository.IAccountRepository;
import com.example.demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IUserRepository userRepository;
    @Override
    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public boolean save(Account account) {
        return accountRepository.save(account) != null;
    }

    @Override
    public boolean update(Account account) {
        return accountRepository.save(account) != null;
    }

    @Override
    public boolean delete(int id) {
        Account account = findById(id);
        if (account == null) {
            return false;
        }
        accountRepository.delete(account);
        return true;
    }

    @Override
    public Account findById(int id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Page<AccountDetailDto> searchAll(String name, String username, String email, String phone, Pageable pageable) {
        return accountRepository.searchAll(name, username, email, phone, pageable);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
