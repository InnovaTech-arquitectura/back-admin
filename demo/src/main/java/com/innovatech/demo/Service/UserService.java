package com.innovatech.demo.Service;

import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements CrudService<UserEntity, Long> {

    @Autowired
    private UserRepository userRepository;

    //I had to change this here so that I could save the entity in a “good way” otherwise it would not let me test things. 
    public UserEntity save(UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }
    

    @Override
    public UserEntity findById(Long id) {
        return  userRepository.findById(id).orElse(null);
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

}
