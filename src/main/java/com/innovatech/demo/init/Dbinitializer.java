package com.innovatech.demo.init;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.demo.Entity.*;
import com.innovatech.demo.Entity.Enum.Modality;
import com.innovatech.demo.Repository.*;
import com.innovatech.demo.Service.*;
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

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EntrepreneurshipeventregistryRepository entrepreneurshipeventregistryRepository;

    // Repositorios adicionales
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private OrderStateRepository orderStateRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponFunctionalityRepository couponFunctionalityRepository;

    @Autowired
    private CouponEntrepreneurshipRepository couponEntrepreneurshipRepository;

    public static final Modality PRESENCIAL = Modality.presencial;
    public static final Modality VIRTUAL = Modality.virtual;

    private static final Logger logger = Logger.getLogger(Dbinitializer.class.getName());

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Roles
        initializeRoles();

        // Usuario Admin
        UserEntity adminUser = createAdminUser();
        createAdminEmployee(adminUser);

        // Funcionalidades y Planes
        createFunctionalitiesAndPlans();

        // Emprendimientos y Usuarios
        insertEntrepreneurships();

        // Eventos
        insertEvents();

        // Cursos
        insertCourses();

        // Subscripciones
        insertSubscriptions();

        // Ciudades y Estados
        initializeStatesAndCities();

        // Estados de Orden
        initializeOrderStates();

        logger.info("All data uploaded.");
    }

    private void initializeRoles() {
        String[] roles = {"Administrator", "Marketing", "Sales", "Community Manager", "Asesor", "Specialist", "Support", "Billing", "Entrepreneurship", "Client"};
        for (String roleName : roles) {
            Role role = new Role();
            role.setName(roleName);
            roleService.save(role);
        }
    }

    private UserEntity createAdminUser() {
        UserEntity adminUser = UserEntity.builder()
                .idCard(123456)
                .name("Andres")
                .email("admin@example.com")
                .password("password123")
                .role(roleService.findByName("Administrator").get())
                .build();
        return userService.save(adminUser);
    }

    private void createAdminEmployee(UserEntity adminUser) {
        AdministrativeEmployee administrativeEmployee = AdministrativeEmployee.builder()
                .user(adminUser)
                .build();
        administrativeEmployeeService.save(administrativeEmployee);
    }

    private void createFunctionalitiesAndPlans() {
        List<Functionality> functionalities = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Functionality functionality = Functionality.builder()
                    .name("Functionality " + i)
                    .description("Description of functionality " + i)
                    .build();
            functionalities.add(functionalityRepository.save(functionality));
        }

        for (int i = 1; i <= 3; i++) {
            Plan plan = Plan.builder()
                    .name("Plan " + i)
                    .price(100.0 + (i * 10))
                    .build();
            plan = planRepository.save(plan);

            for (int j = 0; j < i; j++) {
                PlanFunctionality planFunctionality = PlanFunctionality.builder()
                        .plan(plan)
                        .functionality(functionalities.get(j))
                        .build();
                planFunctionalityRepository.save(planFunctionality);
            }
        }
    }

    private void insertEntrepreneurships() {
        // Lista de emprendimientos con información del usuario
        List<Entrepreneurship> entrepreneurshipList = List.of(
                new Entrepreneurship("Zara", "", "ropa", "Maria", "Martinez"),
                new Entrepreneurship("Nike", "", "deporte", "Juan", "Perez"),
                new Entrepreneurship("Apple", "", "tecnología", "Laura", "Gonzalez"),
                new Entrepreneurship("Bodega El Barril", "", "alimentos", "Carlos", "Lopez"),
                new Entrepreneurship("Yoga Flow", "", "salud", "Sofia", "Martinez"),
                new Entrepreneurship("Travel With Us", "", "turismo", "Jose", "Rodriguez"),
                new Entrepreneurship("Tech Solutions", "tech_logo.png", "Tech company providing innovative solutions", "John", "Doe"),
                new Entrepreneurship("Creative Designs", "design_logo.png", "Graphic and web design services", "Jane", "Smith"),
                new Entrepreneurship("Healthy Eats", "healthy_logo.png", "Organic and healthy food products", "Emily", "Davis")
        );
    
        // Obtén el rol de "Entrepreneurship" para asignarlo a cada usuario creado
        Role entrepreneurshipRole = roleService.findByName("Entrepreneurship")
                .orElseThrow(() -> new RuntimeException("Role 'Entrepreneurship' not found"));
    
        for (Entrepreneurship entrepreneurship : entrepreneurshipList) {
            // Crear un usuario para cada emprendimiento
            UserEntity user = UserEntity.builder()
                    .idCard((int) (Math.random() * 100000)) // ID aleatorio de prueba
                    .name(entrepreneurship.getNames() + " " + entrepreneurship.getLastnames())
                    .email(entrepreneurship.getName().toLowerCase() + "@example.com")
                    .password("1234") // Contraseña predeterminada
                    .role(entrepreneurshipRole)
                    .build();
    
            // Guarda el usuario en la base de datos
            user = userService.save(user);
    
            // Asocia el usuario al emprendimiento y viceversa
            entrepreneurship.setUserEntity(user);
            user.setEntrepreneurship(entrepreneurship);
    
            // Guarda el emprendimiento con el usuario asociado
            entrepreneurshipRepository.save(entrepreneurship);
    
            // Actualiza el usuario con la relación de emprendimiento no nula
            userService.save(user);
        }
    }
    

//     // Método para crear cupones, asociarlos a funcionalidades y al emprendimiento
// private void createCouponsForEntrepreneurship(Entrepreneurship entrepreneurship) {
//     // Funcionalidades que se asociarán a los cupones
//     List<Functionality> functionalities = functionalityRepository.findAll();

//     List<Coupon> coupons = List.of(
//             Coupon.builder()
//                     .description("Cupon de funcionalidades 1")
//                     .expirationDate(Date.from(LocalDate.now().plusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
//                     .expirationPeriod(30)
//                     .entrepreneurship(entrepreneurship)
//                     .build(),
//             Coupon.builder()
//                     .description("Cupon de funcionalidades 2")
//                     .expirationDate(Date.from(LocalDate.now().plusMonths(2).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
//                     .expirationPeriod(60)
//                     .entrepreneurship(entrepreneurship)
//                     .build()
//     );

//     // Guardar cada cupón y asignar funcionalidades
//     for (Coupon coupon : coupons) {
//         coupon = couponRepository.save(coupon);

//         // Asociar algunas funcionalidades al cupón
//         for (int i = 0; i < 3 && i < functionalities.size(); i++) {
//             CouponFunctionality couponFunctionality = new CouponFunctionality(coupon, functionalities.get(i));
//             couponFunctionalityRepository.save(couponFunctionality);
//             coupon.addFunctionality(functionalities.get(i)); // Agrega la funcionalidad al cupón
//         }

//         // Asignar el cupón al emprendimiento mediante CouponEntrepreneurship, con active en false
//         CouponEntrepreneurship couponEntrepreneurship = CouponEntrepreneurship.builder()
//                 .entrepreneurship(entrepreneurship)
//                 .coupon(coupon)
//                 .active(false) // Estado active establecido en false
//                 .build();
//         couponEntrepreneurshipRepository.save(couponEntrepreneurship);
//     }
// }

    private void insertEvents() {
        List<Entrepreneurship> entrepreneurships = entrepreneurshipRepository.findAll();
        for (int i = 1; i <= 5; i++) {
            EventEntity eventEntity = EventEntity.builder()
                    .name("Event " + i)
                    .totalCost(100 + (i * 20))
                    .date(Timestamp.valueOf(LocalDate.now().plusDays(i).atStartOfDay()))
                    .date2(Timestamp.valueOf(LocalDate.now().plusDays(i + 1).atStartOfDay()))
                    .earnings(50 + (i * 10))
                    .costoLocal(30 + (i * 5))
                    .place(i * 10)
                    .modality("Modality " + i)
                    .quota(100)
                    .description("Evento de prueba " + i)
                    .build();

            eventRepository.save(eventEntity);

            for (int j = 0; j < i && j < entrepreneurships.size(); j++) {
                Entrepreneurshipeventregistry entrepreneurshipeventregistry = Entrepreneurshipeventregistry.builder()
                        .eventEntity(eventEntity)
                        .entrepreneurship(entrepreneurships.get(j))
                        .date(eventEntity.getDate())
                        .amountPaid(i * 100000)
                        .build();
                entrepreneurshipeventregistryRepository.save(entrepreneurshipeventregistry);
            }
        }
    }

    private void insertCourses() {
        LocalTime threePM = LocalTime.of(15, 0);
        LocalDateTime tomorrowAtThreePM = LocalDateTime.of(LocalDate.now().plusDays(1), threePM);
        Timestamp timestampForTomorrow = Timestamp.valueOf(tomorrowAtThreePM);

        List<Entrepreneurship> entrepreneurList = entrepreneurshipRepository.findAll();

        Course newCourse = new Course(
                "https://teams.microsoft.com/meetup-link",
                "Capacitación para atraer más clientes",
                0f,
                timestampForTomorrow,
                "Cómo atraer más clientes",
                2,
                VIRTUAL);

        for (Entrepreneurship entrepreneurship : entrepreneurList) {
            courseEntrepreneurshipRepository.save(new CourseEntrepreneurship(entrepreneurship, newCourse));
        }

        courseRepository.save(newCourse);

        LocalDateTime yesterdayAtThreePM = LocalDateTime.of(LocalDate.now().minusDays(1), threePM);
        Timestamp timestampForYesterday = Timestamp.valueOf(yesterdayAtThreePM);

        Course newCourse2 = new Course(
                "https://teams.microsoft.com/meetup-link",
                "Capacitación para ser buen jefe",
                0f,
                timestampForYesterday,
                "Cómo ser buen jefe",
                3,
                VIRTUAL);

        for (Entrepreneurship entrepreneurship : entrepreneurList) {
            courseEntrepreneurshipRepository.save(new CourseEntrepreneurship(entrepreneurship, newCourse2));
        }

        courseRepository.save(newCourse2);
    }

    private void insertSubscriptions() {
        List<Plan> plans = planRepository.findAll();
        List<Entrepreneurship> entrepreneurships = entrepreneurshipRepository.findAll();

        for (int i = 1; i <= 10; i++) {
            Subscription subscription = Subscription.builder()
                    .initialDate(Date.valueOf("2024-01-01"))
                    .expirationDate(Date.valueOf("2025-01-01"))
                    .amount(100.0 + (i * 5))
                    .plan(plans.get(i % plans.size()))
                    .entrepreneurship(entrepreneurships.get(i % entrepreneurships.size()))
                    .build();
            subscriptionRepository.save(subscription);
        }
    }

    private void initializeStatesAndCities() {
        if (stateRepository.count() == 0) {
            List<State> states = List.of(
                    State.builder().name("California").build(),
                    State.builder().name("Texas").build(),
                    State.builder().name("New York").build()
            );
            stateRepository.saveAll(states);
        }

        if (cityRepository.count() == 0) {
            State california = stateRepository.findAll().stream().filter(s -> s.getName().equals("California")).findFirst().orElse(null);
            if (california != null) {
                List<City> cities = List.of(
                        City.builder().name("Los Angeles").state(california).build(),
                        City.builder().name("San Francisco").state(california).build()
                );
                cityRepository.saveAll(cities);
            }
        }
    }

    private void initializeOrderStates() {
        if (orderStateRepository.count() == 0) {
            List<OrderState> orderStates = List.of(
                    OrderState.builder().state("Pending").build(),
                    OrderState.builder().state("Shipped").build(),
                    OrderState.builder().state("Delivered").build(),
                    OrderState.builder().state("Cancelled").build()
            );
            orderStateRepository.saveAll(orderStates);
        }
    }
}
