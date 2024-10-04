package com.innovatech.demo.init;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Course;
import com.innovatech.demo.Entity.CourseEntrepreneurship;
import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Entrepreneurshipeventregistry;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Entity.PlanFunctionality;
import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Entity.Subscription;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Entity.Enum.Modality;
import com.innovatech.demo.Repository.CourseEntrepreneurshipRepository;
import com.innovatech.demo.Repository.CourseRepository;
import com.innovatech.demo.Repository.EntrepreneurshipeventregistryRepository;
import com.innovatech.demo.Repository.EventRepository;
import com.innovatech.demo.Repository.FunctionalityRepository;
import com.innovatech.demo.Repository.PlanFunctionalityRepository;
import com.innovatech.demo.Repository.PlanRepository;
import com.innovatech.demo.Repository.RepositoryEntrepreneurship;
import com.innovatech.demo.Repository.SubscriptionRepository;
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
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private FunctionalityRepository functionalityRepository;

    @Autowired
    private PlanFunctionalityRepository planFunctionalityRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RepositoryEntrepreneurship entrepreneurshipRepository;

    @Autowired
    private CourseEntrepreneurshipRepository courseEntrepreneurshipRepository;

    public static final Modality PRESENCIAL = Modality.presencial;
    public static final Modality VIRTUAL = Modality.virtual;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EntrepreneurshipeventregistryRepository entrepreneurshipeventregistryRepository;

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
        for (int i = 1; i <= 3; i++) {
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

        System.out.println("Uploading entrepreneurships");
        insertEntrepreneurships();


        List<Plan> plans = planRepository.findAll();
        List<Entrepreneurship> entrepreneurships = entrepreneurshipRepository.findAll();

        // Creating and saving 10 subscriptions linked to plans and entrepreneurships
        for (int i = 1; i <= 10; i++) {
            Subscription subscription = Subscription.builder()
                    .initialDate(Date.valueOf("2024-01-01"))
                    .expirationDate(Date.valueOf("2025-01-01"))
                    .amount(100.0 + (i * 5))
                    .plan(plans.get(i % plans.size())) // Link to a plan
                    .entrepreneurship(entrepreneurships.get(i % entrepreneurships.size())) // Link to an entrepreneurship
                    .build();

            // Saving the subscription
            subscriptionRepository.save(subscription);
        }

        
        System.out.println("Uploading events");
        insertEvents();


        System.out.println("Uploading courses");
        insertCourses();

        System.out.println("all data uploaded");
    }


     

    private void insertEntrepreneurships() {

    
        // Crear emprendimientos
        Entrepreneurship zara = new Entrepreneurship("Zara", "", "ropa", "maria", "martinez");
        Entrepreneurship nike = new Entrepreneurship("Nike", "", "deporte", "juan", "perez");
        Entrepreneurship apple = new Entrepreneurship("Apple", "", "tecnología", "laura", "gonzalez");
        Entrepreneurship bodegaElBarril = new Entrepreneurship("Bodega El Barril", "", "alimentos", "carlos", "lopez");
        Entrepreneurship yogaFlow = new Entrepreneurship("Yoga Flow", "", "salud", "sofia", "martinez");
        Entrepreneurship travelWithUs = new Entrepreneurship("Travel With Us", "", "turismo", "jose", "rodriguez");

        // Guardar emprendimientos
        entrepreneurshipRepository.save(apple);
        entrepreneurshipRepository.save(bodegaElBarril);
        entrepreneurshipRepository.save(yogaFlow);
        entrepreneurshipRepository.save(travelWithUs);
        entrepreneurshipRepository.save(zara);
        entrepreneurshipRepository.save(nike);

    }
    

    private void insertEvents() {
        // Obtener la lista de emprendimientos desde el repositorio
        List<Entrepreneurship> entrepreneurships = entrepreneurshipRepository.findAll();
    
        // Inicialización de eventos
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
                    .Description("Evento de prueba " + i)
                    .build();
    
            eventRepository.save(eventEntity); // Guardar el evento en el repositorio
    
            // Crear asociaciones con emprendimientos
            for (int j = 0; j < i && j < entrepreneurships.size(); j++) { 
                Entrepreneurshipeventregistry entrepreneurshipeventregistry = Entrepreneurshipeventregistry.builder()
                        .eventEntity(eventEntity)
                        .entrepreneurship(entrepreneurships.get(j)) 
                        .date(Date.valueOf(eventEntity.getDate()))
                        .amountPaid(i * 100.000)
                        .build();
    
                entrepreneurshipeventregistryRepository.save(entrepreneurshipeventregistry); 
            }
        }
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
            courseEntrepreneurshipRepository.save(new CourseEntrepreneurship(entrepreneurship, newCourse));
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
            courseEntrepreneurshipRepository.save(new CourseEntrepreneurship(entrepreneurship, newCourse2));
        }

        // Guardar el nuevo curso en el repositorio
        courseRepository.save(newCourse2);
    }
}
