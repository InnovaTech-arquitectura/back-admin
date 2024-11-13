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
    private EntrepreneurshipService entrepreneurshipService;

    @Autowired
    private AdministrativeEmployeeService administrativeEmployeeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private OrderService orderService;

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

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private UserRepository userEntityRepository;

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
        insertEntrepreneurshipsAndUsers();

        // Productos
        insertProductsForEntrepreneurships();

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

        // Ordenes
        insertOrders();

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

    private void insertEntrepreneurshipsAndUsers() {
        System.out.println("Initializing database with sample users and entrepreneurship data...");
    
        // Obtener el rol "Entrepreneurship" para asignarlo a cada usuario creado
        Role entrepreneurshipRole = roleService.findByName("Entrepreneurship")
                .orElseThrow(() -> new RuntimeException("Role 'Entrepreneurship' not found"));
    
        // Crear primer cliente
        UserEntity user1 = UserEntity.builder()
            .idCard(12345)
            .name("John Doe")
            .email("johndoe@example.com")
            .password("password123")
            .role(entrepreneurshipRole)  // Asignar el rol
            .build();
        user1 = userService.save(user1);  // Guardar antes de asociar

        Client client1 = Client.builder()
            .userEntity(user1)
            .id_card("54353453")
            .build();
        clientService.save(client1);

        // Crear emprendimientos y asociarlos con los usuarios ya guardados
        Entrepreneurship ent1 = new Entrepreneurship("Tech Solutions", "tech_logo.png", 
            "Tech company providing innovative solutions", "John", "Doe");
        ent1.setUserEntity(user1);

        entrepreneurshipService.save(ent1);
        user1.setEntrepreneurship(ent1);
        userService.save(user1);

        // Crear segundo cliente
        UserEntity user2 = UserEntity.builder()
            .idCard(67890)
            .name("Jane Smith")
            .email("janesmith@example.com")
            .password("password123")
            .role(entrepreneurshipRole)  // Asignar el rol
            .build();
        user2 = userService.save(user2);  // Guardar antes de asociar

        Client client2 = Client.builder()
            .userEntity(user2)
            .id_card("12345678")
            .build();
        clientService.save(client2);

        Entrepreneurship ent2 = new Entrepreneurship("Creative Designs", "design_logo.png", 
            "Graphic and web design services", "Jane", "Smith");
        ent2.setUserEntity(user2);

        entrepreneurshipService.save(ent2);
        user2.setEntrepreneurship(ent2);
        userService.save(user2);

        // Crear tercer cliente
        UserEntity user3 = UserEntity.builder()
            .idCard(11223)
            .name("Emily Davis")
            .email("emilydavis@example.com")
            .password("password123")
            .role(entrepreneurshipRole)  // Asignar el rol
            .build();
        user3 = userService.save(user3);  // Guardar antes de asociar

        Client client3 = Client.builder()
            .userEntity(user3)
            .id_card("87654321")
            .build();
        clientService.save(client3);

        Entrepreneurship ent3 = new Entrepreneurship("Healthy Eats", "healthy_logo.png", 
            "Organic and healthy food products", "Emily", "Davis");
        ent3.setUserEntity(user3);

        entrepreneurshipService.save(ent3);
        user3.setEntrepreneurship(ent3);
        userService.save(user3);
    }

    private void insertProductsForEntrepreneurships() {
        if (productRepository.count() == 0) {
            List<Entrepreneurship> entrepreneurships = entrepreneurshipRepository.findAll();
            
            Product product1 = new Product();
            product1.setName("Product 1");
            product1.setQuantity(10);
            product1.setPrice(20.0);
            product1.setCost(15.0);
            product1.setDescription("Description for Product 1");
            product1.setMultimedia("p-1");
            product1.setEntrepreneurship(entrepreneurships.get(0));

            Product product2 = new Product();
            product2.setName("Product 2");
            product2.setQuantity(5);
            product2.setPrice(35.0);
            product2.setCost(25.0);
            product2.setDescription("Description for Product 2");
            product2.setMultimedia("p-2");
            product2.setEntrepreneurship(entrepreneurships.get(1));

            Product product3 = new Product();
            product3.setName("Product 3");
            product3.setQuantity(8);
            product3.setPrice(50.0);
            product3.setCost(40.0);
            product3.setDescription("Description for Product 3");
            product3.setMultimedia("p-3");
            product3.setEntrepreneurship(entrepreneurships.get(2));

            productRepository.saveAll(List.of(product1, product2, product3));

            // Actualizar el valor multimedia después de guardar
            product1.setMultimedia("p-" + product1.getId());
            product2.setMultimedia("p-" + product2.getId());
            product3.setMultimedia("p-" + product3.getId());
            productRepository.saveAll(List.of(product1, product2, product3));
        }
    }

    private void insertOrders() {
        // Obtener los estados de orden
        List<OrderState> orderStates = orderStateRepository.findAll();
        if (orderStates.isEmpty()) {
            throw new RuntimeException("Order states must be initialized first.");
        }
    
        // Obtener las ciudades
        List<City> cities = cityRepository.findAll();
        if (cities.isEmpty()) {
            throw new RuntimeException("Cities must be initialized first.");
        }
    
        // Obtener los clientes
        List<Client> clients = clientService.findAll();
        if (clients.isEmpty()) {
            throw new RuntimeException("Clients must be initialized first.");
        }
    
        // Crear órdenes para cada cliente
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            City city = cities.get(i % cities.size()); // Distribuir ciudades entre órdenes
            OrderState orderState = orderStates.get(i % orderStates.size()); // Distribuir estados entre órdenes
    
            Order order = Order.builder()
                    .sale_number("SN" + (i + 1))
                    .additional_info("Additional info for order " + (i + 1))
                    .address("1234 Street, City " + city.getName())
                    .orderState(orderState) // Estado de la orden
                    .city(city)             // Ciudad de la orden
                    .build();
    
            // Guardar la orden en el repositorio
            orderService.save(order);
        }
    
        logger.info("Orders initialized successfully.");
    }
    



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
