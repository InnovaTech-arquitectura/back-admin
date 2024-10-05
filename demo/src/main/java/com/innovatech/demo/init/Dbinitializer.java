package com.innovatech.demo.init;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Course;
import com.innovatech.demo.Entity.Coupon;
import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Entity.PlanFunctionality;
import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Entity.Enum.Modality;
import com.innovatech.demo.Repository.CourseRepository;
import com.innovatech.demo.Repository.CouponRepository;
import com.innovatech.demo.Repository.EventRepository;
import com.innovatech.demo.Repository.FunctionalityRepository;
import com.innovatech.demo.Repository.PlanFunctionalityRepository;
import com.innovatech.demo.Repository.PlanRepository;
import com.innovatech.demo.Repository.RepositoryEntrepreneurship;
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

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RepositoryEntrepreneurship entrepreneurshipRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CouponRepository couponRepository;

    public static final Modality PRESENCIAL = Modality.presencial;
    public static final Modality VIRTUAL = Modality.virtual;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Seed roles, users, and administrative employees
        seedRolesAndUsers();

        // Create and save functionalities
        List<Functionality> functionalities = createFunctionalities();

        // Create and save plans linked to functionalities
        List<Plan> plans = createPlansAndLinkFunctionalities(functionalities);

        // Seed events
        insertEvents();

        // Seed entrepreneurships
        insertEntrepreneurships();

        // Seed courses
        insertCourses();

        // Seed coupons and associate them with plans
        insertCoupons(plans);

        System.out.println("All data uploaded.");
    }

    private void seedRolesAndUsers() {
        // Create roles
        List<String> roles = List.of("Administrator", "Marketing", "Sales", "Community Manager", "Asesor", "Specialist", "Support", "Billing", "Entrepreneurship", "Client");

        for (String roleName : roles) {
            Role role = new Role();
            role.setName(roleName);
            roleService.save(role);
        }

        // Create and save admin user
        UserEntity adminUser = UserEntity.builder()
                .idCard(123456)
                .name("Andres")
                .email("admin@example.com")
                .password("password123")
                .role(roleService.findByName("Administrator").get())
                .build();

        userService.save(adminUser);

        AdministrativeEmployee administrativeEmployee = AdministrativeEmployee.builder()
                .user(adminUser)
                .build();

        administrativeEmployeeService.save(administrativeEmployee);
    }

    private List<Functionality> createFunctionalities() {
        List<Functionality> functionalities = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Functionality functionality = Functionality.builder()
                    .name("Functionality " + i)
                    .description("Description of functionality " + i)
                    .build();
            functionalities.add(functionalityRepository.save(functionality));
        }
        return functionalities;
    }

    private List<Plan> createPlansAndLinkFunctionalities(List<Functionality> functionalities) {
        List<Plan> plans = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            Plan plan = Plan.builder()
                    .name("Plan " + i)
                    .price(100.0 + (i * 10))
                    .build();

            plan = planRepository.save(plan);

            // Link functionalities to each plan
            for (int j = 0; j < i; j++) {
                PlanFunctionality planFunctionality = PlanFunctionality.builder()
                        .plan(plan)
                        .functionality(functionalities.get(j))
                        .build();
                planFunctionalityRepository.save(planFunctionality);
            }
            plans.add(plan);
        }
        return plans;
    }

    private void insertEvents() {
        for (int i = 1; i <= 5; i++) {
            EventEntity eventEntity = EventEntity.builder()
                    .name("Event " + i)
                    .totalCost(100 + (i * 20))
                    .date(LocalDate.now().plusDays(i).toString())
                    .earnings(50 + (i * 10))
                    .costoLocal(30 + (i * 5))
                    .place("Place " + i)
                    .modality("Modality " + i)
                    .quota(100)
                    .build();
            eventRepository.save(eventEntity);
        }
    }

    private void insertEntrepreneurships() {
        Entrepreneurship zara = new Entrepreneurship("Zara", "", "ropa", "maria", "martinez");
        Entrepreneurship nike = new Entrepreneurship("Nike", "", "deporte", "juan", "perez");

        entrepreneurshipRepository.save(zara);
        entrepreneurshipRepository.save(nike);
    }

    private void insertCourses() {
        LocalTime threePM = LocalTime.of(15, 0);

        LocalDateTime tomorrowAtThreePM = LocalDateTime.of(LocalDate.now().plusDays(1), threePM);
        Timestamp timestampForTomorrow = Timestamp.valueOf(tomorrowAtThreePM);

        List<Entrepreneurship> entrepreneurList = entrepreneurshipRepository.findAll();

        Course newCourse = new Course(
                "https://teams.microsoft.com/l/meetup-join/...",
                "Capacitación para atraer más clientes",
                0f,
                timestampForTomorrow,
                "Cómo atraer más clientes",
                2,
                VIRTUAL);

        for (Entrepreneurship entrepreneurship : entrepreneurList) {
            newCourse.addEntrepreneurship(entrepreneurship);
        }
        courseRepository.save(newCourse);
    }

    private void insertCoupons(List<Plan> plans) {
        List<Entrepreneurship> entrepreneurList = entrepreneurshipRepository.findAll();

        for (Entrepreneurship entrepreneurship : entrepreneurList) {
            for (int i = 0; i < plans.size(); i++) {
                Plan associatedPlan = plans.get(i);

                Coupon coupon = Coupon.builder()
                        .description((i + 1) * 10 + "% de descuento en productos de " + entrepreneurship.getName())
                        .expirationDate(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30 * (i + 1)))) // 30, 60, 90 days
                        .expirationPeriod((i + 1) * 30)
                        .entrepreneurship(entrepreneurship)
                        .plan(associatedPlan)  // Associate with a plan
                        .build();

                couponRepository.save(coupon);
            }
        }
    }
}

