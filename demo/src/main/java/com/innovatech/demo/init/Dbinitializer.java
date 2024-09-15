package com.innovatech.demo.init;

import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Service.AdministrativeEmployeeService;
import com.innovatech.demo.Service.RoleService;
import com.innovatech.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Dbinitializer implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdministrativeEmployeeService administrativeEmployeeService;

    @Override
    @Transactional 
    public void run(String... args) throws Exception {
        Role adminRole = new Role();
        adminRole.setName("Administrative Employee");
        roleService.save(adminRole);

        Role clientRole = new Role();
        clientRole.setName("Client");
        roleService.save(clientRole);

        Role entrepreneurRole = new Role();
        entrepreneurRole.setName("Entrepreneurship");
        roleService.save(entrepreneurRole);

        UserEntity adminUser = UserEntity.builder()
                .idCard(123456)
                .name("Andres")
                .email("hitler@example.com")
                .password("password123")
                .role(adminRole)
                .build();

        adminUser = userService.save(adminUser);

        AdministrativeEmployee administrativeEmployee = AdministrativeEmployee.builder()
                .user(adminUser) 
                .build();

        administrativeEmployee = administrativeEmployeeService.save(administrativeEmployee);

        adminUser.setAdministrativeEmployee(administrativeEmployee);
        userService.save(adminUser);

        UserEntity user1 = UserEntity.builder()
                .idCard(654321)
                .name("Nicolas")
                .email("jesus@example.com")
                .password("password456")
                .role(clientRole)
                .build();
        userService.save(user1); 

        UserEntity user2 = UserEntity.builder()
                .idCard(789012)
                .name("Chamo")
                .email("maduro@example.com")
                .password("password789")
                .role(entrepreneurRole)
                .build();
        userService.save(user2); 
    }
}
