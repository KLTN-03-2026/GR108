package com.example.demo.service;

import com.example.demo.emumm.RoleName;
import com.example.demo.entity.Role;
import com.example.demo.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName).orElse(null);
    }
}
