package com.example.demo.service;

import com.example.demo.emumm.RoleName;
import com.example.demo.entity.Role;

public interface IRoleService {
    void save(Role role);
    Role findByRoleName(RoleName roleName);
}