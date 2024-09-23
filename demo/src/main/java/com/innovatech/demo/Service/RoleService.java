package com.innovatech.demo.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

}
