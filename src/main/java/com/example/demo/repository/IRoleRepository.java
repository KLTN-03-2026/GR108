package com.example.demo.repository;

import com.example.demo.entity.Role;
import com.example.demo.emumm.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Integer> {
    Role findByRollName(RoleName rollName);
}
