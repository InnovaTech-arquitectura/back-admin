package com.innovatech.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Repository.UserRepository;

@Service
public class UserService implements CrudService<UserEntity, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // I had to change this here so that I could save the entity in a “good way”
    // otherwise it would not let me test things.

    public UserEntity save(UserEntity userEntity) {

        String password = userEntity.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        userEntity.setPassword(encodedPassword);
        return userRepository.saveAndFlush(userEntity);
    }

    public UserEntity update(UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByIdCard(int idCard) {
        return userRepository.existsByIdCard(idCard);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public int countByRoleName(String roleName) {
        return userRepository.countByRoleName(roleName);
    }

}
