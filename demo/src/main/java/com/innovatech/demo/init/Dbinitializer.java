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
    @Transactional // Asegura la transacción
    public void run(String... args) throws Exception {
        // Crear roles
        Role adminRole = new Role();
        adminRole.setName("Administrative Employee");
        roleService.save(adminRole);

        Role clientRole = new Role();
        clientRole.setName("Client");
        roleService.save(clientRole);

        Role entrepreneurRole = new Role();
        entrepreneurRole.setName("Entrepreneurship");
        roleService.save(entrepreneurRole);

        // Crear y guardar un usuario asociado a Administrative Employee
        UserEntity adminUser = UserEntity.builder()
                .idCard(123456)
                .name("Andres")
                .email("hitler@example.com")
                .password("password123")
                .role(adminRole)
                .build();

        // Guarda el usuario primero
        adminUser = userService.save(adminUser);

        // Crear y asociar el Administrative Employee
        AdministrativeEmployee administrativeEmployee = AdministrativeEmployee.builder()
                .user(adminUser) // Asocia el usuario ya persistido
                .build();

        // Guarda el Administrative Employee
        administrativeEmployee = administrativeEmployeeService.save(administrativeEmployee);

        // Actualiza el usuario con la referencia del Administrative Employee
        adminUser.setAdministrativeEmployee(administrativeEmployee);
        userService.save(adminUser); // Actualiza el usuario para reflejar la asociación bidireccional

        // Crear usuarios adicionales sin relación con Administrative Employee
        UserEntity user1 = UserEntity.builder()
                .idCard(654321)
                .name("Nicolas")
                .email("jesus@example.com")
                .password("password456")
                .role(clientRole)
                .build();
        userService.save(user1); // Guarda sin asociar a Administrative Employee

        UserEntity user2 = UserEntity.builder()
                .idCard(789012)
                .name("Chamo")
                .email("maduro@example.com")
                .password("password789")
                .role(entrepreneurRole)
                .build();
        userService.save(user2); // Guarda sin asociar a Administrative Employee

    }
}
