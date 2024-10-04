package com.innovatech.demo.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Repositorio de usuarios

    private Map<String, String> recoveryCodes = new HashMap<>();

    // Encuentra un usuario por email
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Guarda un usuario, sin codificar la contraseña
    public UserEntity save(UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }

    public UserEntity update(UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByIdCard(int idCard) {
        return userRepository.existsByIdCard(idCard);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public int countByRoleName(String roleName) {
        return userRepository.countByRoleName(roleName);
    }

    public boolean verifyRecoveryCode(String code, String email) {
        // Retrieve and verify recovery code for the specified email
        String storedCode = recoveryCodes.get(email);
        return storedCode != null && storedCode.equals(code);
    }

    public void setRecoveryCode(String email, String code) {
        // Store the recovery code associated with the user's email
        recoveryCodes.put(email, code);
    }
}
