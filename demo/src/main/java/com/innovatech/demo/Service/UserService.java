package com.innovatech.demo.Service;

import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements CrudService<UserEntity, Long> {

    @Autowired
    private UserRepository userRepository;

    public UserEntity save(UserEntity userEntity){
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity findById(Long id) {
        return  userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }



}
