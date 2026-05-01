package com.example.demo.repository;

import com.example.demo.dto.AccountDetailDto;
import com.example.demo.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer> {

    @Query("SELECT new com.example.demo.dto.AccountDetailDto(" +
            "a.username, a.password, u.fullName, u.gender, u.email, u.phone, a.role) " +
            "FROM Account a JOIN a.user u " +
            "WHERE (:name IS NULL OR :name = '' OR u.fullName LIKE %:name%) " +
            "AND (:username IS NULL OR :username = '' OR a.username LIKE %:username%) " +
            "AND (:email IS NULL OR :email = '' OR u.email LIKE %:email%) " +
            "AND (:phone IS NULL OR :phone = '' OR u.phone LIKE %:phone%)")
    Page<AccountDetailDto> searchAll(
            @Param("name") String name,
            @Param("username") String username,
            @Param("email") String email,
            @Param("phone") String phone,
            Pageable pageable
    );

    Account findByUsername(String username);
}