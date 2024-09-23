package com.innovatech.demo.init;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Course;
import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Entity.PlanFunctionality;
import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Entity.Enum.Modality;
import com.innovatech.demo.Repository.CourseRepository;
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

    public static final Modality PRESENCIAL = Modality.presencial;
    public static final Modality VIRTUAL = Modality.virtual;

    @Autowired
    private EventRepository eventRepository;

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

        Role specialistRole = new Role();
        specialistRole.setName("Specialist");
        roleService.save(specialistRole);

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

        // Inicialización de eventos
        for (int i = 1; i <= 5; i++) {
            EventEntity eventEntity = EventEntity.builder()
                    .name("Event " + i)
                    .Total_Cost(100 + (i * 20))
                    .date(LocalDate.now().plusDays(i).toString())
                    .Earnings(50 + (i * 10))
                    .CostoLocal(30 + (i * 5))
                    .place("Place " + i)
                    .modality("Modality " + i)
                    .Quota(100)
                    .build();

            eventRepository.save(eventEntity); // Call the save() method on the eventRepository instance
        }

        System.out.println("Uploading entrepreneurships");
        insertEntrepreneurships();

        System.out.println("Uploading courses");
        insertCourses();

        System.out.println("all data uploaded");
    }

    private void insertEntrepreneurships() {
        Entrepreneurship zara = new Entrepreneurship("Zara", "", "ropa", "maria", "martinez");
        Entrepreneurship nike = new Entrepreneurship("Nike", "", "deporte", "juan", "perez");

        entrepreneurshipRepository.save(zara);
        entrepreneurshipRepository.save(nike);
    }

    private void insertCourses() {
        // Obtener la hora de las 3 PM
        LocalTime threePM = LocalTime.of(15, 0);

        // Crear la fecha de mañana a las 3 PM
        LocalDateTime tomorrowAtThreePM = LocalDateTime.of(LocalDate.now().plusDays(1), threePM);
        Timestamp timestampForTomorrow = Timestamp.valueOf(tomorrowAtThreePM);

        // Obtener la lista de emprendimientos
        List<Entrepreneurship> entrepreneurList = entrepreneurshipRepository.findAll();

        // Crear el nuevo curso
        Course newCourse = new Course(
                "https://teams.microsoft.com/l/meetup-join/19%3ameeting_NTAxZmMyMmItNzUwNS00Mjg0LWEzMTQtZTE0ZDNmZTRkNzQ2%40thread.v2/0?",
                "Capacitación para atraer más clientes",
                0f,
                timestampForTomorrow,
                "Cómo atraer más clientes",
                2,
                VIRTUAL);

        // Añadir los emprendimientos al curso
        for (Entrepreneurship entrepreneurship : entrepreneurList) {
            newCourse.addEntrepreneurship(entrepreneurship);
        }

        // Guardar el nuevo curso en el repositorio
        courseRepository.save(newCourse);

        // Crear la fecha de mañana a las 3 PM
        LocalDateTime yesterdayAtThreePM = LocalDateTime.of(LocalDate.now().minusDays(1), threePM);
        Timestamp timestampForYesterday = Timestamp.valueOf(yesterdayAtThreePM);

        Course newCourse2 = new Course(
                "https://teams.microsoft.com/l/meetup-join/19%3ameeting_NTAxZmMyMmItNzUwNS00Mjg0LWEzMTQtZTE0ZDNmZTRkNzQ2%40thread.v2/0?",
                "Capacitación para ser buen jefe",
                0f,
                timestampForYesterday,
                "Como ser buen jefe",
                3,
                VIRTUAL);

        // Añadir los emprendimientos al curso
        for (Entrepreneurship entrepreneurship : entrepreneurList) {
            newCourse2.addEntrepreneurship(entrepreneurship);
        }

        // Guardar el nuevo curso en el repositorio
        courseRepository.save(newCourse2);
    }
}
