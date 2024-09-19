package com.innovatech.demo.Service;

import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByName(String name){
        return roleRepository.findByName(name);
    }

    public boolean existsByName(String name){
        return roleRepository.existsByName(name);
    }

    public boolean existsById(Long id){
        return roleRepository.existsById(id);
    }

    //this I agree because is necessary to save a role
    public Role save(Role role){
        return roleRepository.save(role);
    }

}
