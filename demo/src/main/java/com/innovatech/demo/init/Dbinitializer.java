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
        adminRole.setName("Administrator");
        roleService.save(adminRole);

        Role marketingRole = new Role();
        marketingRole.setName("Marketing");
        roleService.save(marketingRole);

        Role salesRole = new Role();
        salesRole.setName("Sales");
        roleService.save(salesRole);
        
        Role communityManagerRole = new Role();
        communityManagerRole.setName("Community Manager");
        roleService.save(communityManagerRole);

        Role asesorRole = new Role();
        asesorRole.setName("Asesor");
        roleService.save(asesorRole);

        Role supportRole = new Role();
        supportRole.setName("Support");
        roleService.save(supportRole);

        Role billingRole = new Role();
        billingRole.setName("Billing");
        roleService.save(billingRole);

        Role entrepreneurRole = new Role();
        entrepreneurRole.setName("Entrepreneurship");
        roleService.save(entrepreneurRole);

        Role clientRole = new Role();
        clientRole.setName("Client");
        roleService.save(clientRole);



        UserEntity adminUser = UserEntity.builder()
                .idCard(123456)
                .name("Andres")
                .email("admin@example.com")
                .password("password123")
                .role(roleService.findByName("Administrator").get())
                .build();

        adminUser = userService.save(adminUser);

        AdministrativeEmployee administrativeEmployee = AdministrativeEmployee.builder()
                .user(adminUser) 
                .build();

        administrativeEmployee = administrativeEmployeeService.save(administrativeEmployee);
    }
}
