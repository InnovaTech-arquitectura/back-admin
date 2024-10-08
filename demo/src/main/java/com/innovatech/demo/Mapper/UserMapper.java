package com.innovatech.demo.Mapper;

import com.innovatech.demo.Entity.UserEntity; // Import your UserEntity class
import com.innovatech.demo.DTO.UserDTO; // Import your UserDTO class

public class UserMapper {
    
    // Method to convert UserDTO to UserEntity
    public static UserEntity toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());
        // Map other fields as necessary
        
        return userEntity;
    }

    // Method to convert UserEntity to UserDTO
    public static UserDTO toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPassword(userEntity.getPassword());
        // Map other fields as necessary
        
        return userDTO;
    }
}