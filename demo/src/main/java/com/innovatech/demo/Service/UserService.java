package com.innovatech.demo.Service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Repositorio de usuarios

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<String, String> recoveryCodes = new HashMap<>();

    // Encuentra un usuario por email
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Guarda un usuario, codificando la contraseña
    public UserEntity save(UserEntity userEntity) {
        String password = userEntity.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        userEntity.setPassword(encodedPassword);
        return userRepository.saveAndFlush(userEntity);
    }


    // Actualiza un usuario, codificando la contraseña si es necesario
    public UserEntity update(UserEntity userEntity) {
        if (userEntity.getPassword() != null) {
            String password = userEntity.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            userEntity.setPassword(encodedPassword);
        }
        return userRepository.saveAndFlush(userEntity);
    }

    // Encuentra un usuario por ID

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Elimina un usuario por ID
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // Verifica si un usuario existe por cédula
    public boolean existsByIdCard(int idCard) {
        return userRepository.existsByIdCard(idCard);
    }

    // Verifica si un usuario existe por email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Cuenta el número de usuarios por rol
    public int countByRoleName(String roleName) {
        return userRepository.countByRoleName(roleName);
    }

    // Verifica si el código de recuperación es válido para un email
    public boolean verifyRecoveryCode(String code, String email) {
        String storedCode = recoveryCodes.get(email);
        return storedCode != null && storedCode.equals(code);
    }

    // Establece un código de recuperación para un email
    public void setRecoveryCode(String email, String code) {
        recoveryCodes.put(email, code);
    }
}
