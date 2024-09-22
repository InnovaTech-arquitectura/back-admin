package com.innovatech.demo.init;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Entity.PlanFunctionality;
import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Repository.FunctionalityRepository;
import com.innovatech.demo.Repository.PlanFunctionalityRepository;
import com.innovatech.demo.Repository.PlanRepository;
import com.innovatech.demo.Service.AdministrativeEmployeeService;
import com.innovatech.demo.Service.RoleService;
import com.innovatech.demo.Service.UserService;

@Component
public class Dbinitializer implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdministrativeEmployeeService administrativeEmployeeService;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private FunctionalityRepository functionalityRepository;

    @Autowired
    private PlanFunctionalityRepository planFunctionalityRepository;

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

        // Creating and saving functionalities
        List<Functionality> functionalities = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Functionality functionality = Functionality.builder()
                    .name("Functionality " + i)
                    .description("Description of functionality " + i)
                    .build();

            functionalities.add(functionalityRepository.save(functionality));
        }

        // Creating and saving 10 plans and linking them to functionalities
        for (int i = 1; i <= 10; i++) {
            Plan plan = Plan.builder()
                    .name("Plan " + i)
                    .price(100.0 + (i * 10))
                    .build();

            // Saving the plan
            plan = planRepository.save(plan);

            // Creating associations with functionalities
            for (int j = 0; j < i; j++) {
                PlanFunctionality planFunctionality = PlanFunctionality.builder()
                        .plan(plan)
                        .functionality(functionalities.get(j))
                        .build();
                planFunctionalityRepository.save(planFunctionality);
            }
        }
    }
}
