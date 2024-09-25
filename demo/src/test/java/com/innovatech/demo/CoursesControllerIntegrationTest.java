package com.innovatech.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.innovatech.demo.DTO.CourseDTONoID;
import com.innovatech.demo.DTO.CourseInfoDTO;
import com.innovatech.demo.DTO.UserDTO;
import com.innovatech.demo.Entity.Course;
import com.innovatech.demo.Entity.Enum.Modality;
import com.innovatech.demo.Repository.CourseRepository;

@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class CoursesControllerIntegrationTest {

    private static final String SERVER_URL = "http://localhost:8090";

    @Autowired
    private CourseRepository courseRepository;

    public static final Modality PRESENCIAL = Modality.presencial;
    public static final Modality VIRTUAL = Modality.virtual;

    @BeforeEach
    @Transactional
    @DirtiesContext
    void init() {

    }

    @Autowired
    private TestRestTemplate rest;

    // test funcionamiento de pages
    @Test
    public void testGetCoursesPageable() {

        UserDTO user = new UserDTO();
        user.setEmail("admin@example.com");
        user.setPassword("password123");

        // hacer el post del log in
        ResponseEntity<String> response = rest.postForEntity(SERVER_URL + "/login", user, String.class);

        System.err.println(response.getBody());
        rest.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + response.getBody());
            return execution.execute(request, body);
        }));

        // Crear el objeto de solicitud con el token JWT en el encabezado de
        // autorización
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(response.getBody());

        String url = SERVER_URL + "/course/all?page=1&limit=1";
        Course[] courses = rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Course[].class).getBody();
        // Course[] courses = rest.getForObject(url, Course[].class);

        assert courses != null;
        assert courses.length == 1;

        String url2 = SERVER_URL + "/course/all?page=2&limit=1";

        Course[] courses2 = rest.exchange(
                url2,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Course[].class).getBody();

        assert courses2 != null;
        assert courses2.length == 1;
        assert courses[0].getId() != courses2[0].getId();
    }

    // test funcionamiento de que solo traiga capacitaciones activas
    @Test
    public void onlyActiveCourses() {

        UserDTO user = new UserDTO();
        user.setEmail("admin@example.com");
        user.setPassword("password123");

        // hacer el post del log in
        ResponseEntity<String> response = rest.postForEntity(SERVER_URL + "/login", user, String.class);

        System.err.println(response.getBody());
        rest.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + response.getBody());
            return execution.execute(request, body);
        }));

        // Crear el objeto de solicitud con el token JWT en el encabezado de
        // autorización
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(response.getBody());

        String url = SERVER_URL + "/course/all/active?page=1&limit=2";

        Course[] courses = rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Course[].class).getBody();

        assert courses != null;
        // assert courses.length == 1;

        // Verificar que todas las fechas de los cursos obtenidos sean mayores que la
        // fecha actual
        for (Course course : courses) {
            LocalDateTime courseDate = course.getDate().toLocalDateTime(); // Convierte Timestamp a LocalDateTime
            assert courseDate.isAfter(LocalDateTime.now());
        }

    }

    // test de que en editar ponga valores adecuados y que en cupos no
    // ponga un valor menor a la cantidad que esta inscritos
    // la fecha no debe ser antes de hoy
    @Test
    public void testEditCourseWrongDate() {
        // Obtener el curso existente
        Course course = courseRepository.findById(1L).get();

        // Crear un objeto CourseDTONoID con los valores que deseas editar
        CourseDTONoID editedCourseDto = new CourseDTONoID();
        editedCourseDto.setLink(course.getLink());
        editedCourseDto.setDescription(course.getDescription());
        editedCourseDto.setScore(course.getScore());
        editedCourseDto.setDate(Timestamp.valueOf(LocalDateTime.now().minusDays(1))); // Establece una fecha futura
        editedCourseDto.setTitle(course.getTitle());
        editedCourseDto.setPlaces(3);
        editedCourseDto.setModality(PRESENCIAL);

        UserDTO user = new UserDTO();
        user.setEmail("admin@example.com");
        user.setPassword("password123");

        // hacer el post del log in
        ResponseEntity<String> responseLogIn = rest.postForEntity(SERVER_URL + "/login", user, String.class);

        System.err.println(responseLogIn.getBody());
        rest.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + responseLogIn.getBody());
            return execution.execute(request, body);
        }));

        // Crear el objeto de solicitud con el token JWT en el encabezado de
        // autorización
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(responseLogIn.getBody());

        HttpEntity<CourseDTONoID> requestEntity = new HttpEntity<>(editedCourseDto, headers);

        // Realiza la solicitud PUT
        ResponseEntity<?> response = rest.exchange(
                SERVER_URL + "/course/{id}",
                HttpMethod.PUT,
                requestEntity,
                String.class,
                1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Places must be greater than 0 and date muyt be greater than todays date", response.getBody());
    }

    @Test
    public void testEditCourseWrongCapacity() {
        // Obtener el curso existente
        Course course = courseRepository.findById(1L).get();

        // Crear un objeto CourseDTONoID con los valores que deseas editar
        CourseDTONoID editedCourseDto = new CourseDTONoID();
        editedCourseDto.setLink(course.getLink());
        editedCourseDto.setDescription(course.getDescription());
        editedCourseDto.setScore(course.getScore());
        editedCourseDto.setDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1))); // Establece una fecha futura
        editedCourseDto.setTitle(course.getTitle());
        editedCourseDto.setPlaces(1);
        editedCourseDto.setModality(PRESENCIAL);

        UserDTO user = new UserDTO();
        user.setEmail("admin@example.com");
        user.setPassword("password123");

        // hacer el post del log in
        ResponseEntity<String> responseLogIn = rest.postForEntity(SERVER_URL + "/login", user, String.class);

        System.err.println(responseLogIn.getBody());
        rest.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + responseLogIn.getBody());
            return execution.execute(request, body);
        }));

        // Crear el objeto de solicitud con el token JWT en el encabezado de
        // autorización
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(responseLogIn.getBody());

        HttpEntity<CourseDTONoID> requestEntity = new HttpEntity<>(editedCourseDto, headers);

        // Realiza la solicitud PUT
        ResponseEntity<?> response = rest.exchange(
                SERVER_URL + "/course/{id}",
                HttpMethod.PUT,
                requestEntity,
                String.class,
                1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Edited places must be equal o greater compared to the current subscribers", response.getBody());
    }

    // test de que me encuentre la capacitacion y me muestra los cupos disponibles
    // de cantidad de empresas disponibles vs cupos disponibles
    @Test
    public void getCourse() {
        UserDTO user = new UserDTO();
        user.setEmail("admin@example.com");
        user.setPassword("password123");

        // hacer el post del log in
        ResponseEntity<String> response = rest.postForEntity(SERVER_URL + "/login", user, String.class);

        System.err.println(response.getBody());
        rest.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + response.getBody());
            return execution.execute(request, body);
        }));

        // Crear el objeto de solicitud con el token JWT en el encabezado de
        // autorización
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(response.getBody());

        String url = SERVER_URL + "/course/2";

        CourseInfoDTO course = rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CourseInfoDTO.class).getBody();

        assert course != null;
        assert course.getPlaces() == 3;
        assert course.getAvailablePlaces() == 1;
    }

}
