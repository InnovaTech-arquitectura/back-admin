package com.innovatech.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.MediaType;

import com.innovatech.demo.Controller.PublicationController;
import com.innovatech.demo.DTO.BannerDTO;
import com.innovatech.demo.DTO.BannerInfoDTO;
import com.innovatech.demo.DTO.UserDTO;
import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Banner;
import com.innovatech.demo.Repository.AdministrativeEmployeeRepository;
import com.innovatech.demo.Repository.BannerRepository;
import com.innovatech.demo.Service.PublicationService;
import com.innovatech.demo.Service.UserService;

import io.jsonwebtoken.io.IOException;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class PublicationControllerTest {

    private static final String SERVER_URL = "http://localhost:8090";

    @Autowired
    private PublicationController publicationController;
    
    @Autowired
    private TestRestTemplate rest;

    private ResponseEntity<String> response=null;

    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    private BannerRepository bannerRepository;
    
    @Autowired
    private AdministrativeEmployeeRepository administrativeEmployeeRepository;


    @BeforeEach
    public void setUp() {

        UserDTO user = new UserDTO();
        user.setEmail("admin@example.com");
        user.setPassword("password123");

        //hacer el post del log in
        response = rest.postForEntity(SERVER_URL + "/login", user, String.class);

        System.err.println(response.getBody());
        rest.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + response.getBody());
            return execution.execute(request, body);
        }));

        // Crear el objeto de solicitud con el token JWT en el encabezado de autorización
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(response.getBody());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        
        Optional<AdministrativeEmployee> adminOpt = administrativeEmployeeRepository.findById(1L);
        
        if (adminOpt.isPresent()) {
            Banner banner = new Banner("prueba titulo", "prueba titulo", adminOpt.get());
            //System.out.println("AdministrativeEmployee found with id: " + banner.getTitle());
            Banner created=bannerRepository.save(banner);

            created.setMultimedia("p-"+created.getId().toString());

            bannerRepository.save(created);
        } 

    }
    

    @Test
    public void testCreateABanner() throws IOException, java.io.IOException {
       
        testCreateABannerFunc();
    }

    public void testCreateABannerFunc() throws IOException, java.io.IOException {
        // Crear archivo de prueba
        MockMultipartFile mockFile = new MockMultipartFile(
                "picture",  // El nombre debe coincidir con el nombre del campo en el controlador
                "test-image.jpg",
                "image/jpeg",
                "Test Image Content".getBytes()
        );

        // Crear los parámetros del cuerpo de la solicitud como MultiValueMap
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", "prueba titulo 2");  // Añadir el título del banner
        body.add("adminId", "1");  // Añadir ID del empleado administrativo
        body.add("picture", new ByteArrayResource(mockFile.getBytes()) {  // Añadir el archivo
            @Override
            public String getFilename() {
                return mockFile.getOriginalFilename();  // Definir el nombre del archivo
            }
        });

        // Establecer headers para multipart/form-data
        //headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        System.err.println("REVISAR BIEN "+headers);

        // Crear la entidad de la solicitud
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Enviar la solicitud usando TestRestTemplate
        ResponseEntity<?> response = rest.exchange(
            SERVER_URL + "/banner/new",
            HttpMethod.POST,
            requestEntity,
            Banner.class
        );

        // Verificar la respuesta (aserciones)
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



    @Test
    public void testGetAllBanners() throws IOException, java.io.IOException {
        
        UserDTO user = new UserDTO();
        user.setEmail("admin@example.com");
        user.setPassword("password123");

        //hacer el post del log in
        ResponseEntity<String> response = rest.postForEntity(SERVER_URL + "/login", user, String.class);

        System.err.println(response.getBody());
        rest.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + response.getBody());
            return execution.execute(request, body);
        }));

        // Crear el objeto de solicitud con el token JWT en el encabezado de autorización
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(response.getBody());
        
        String url = "http://localhost:8090/banner/all";

        System.err.println("unicornio status "+ rest.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            BannerInfoDTO[].class
        ).getStatusCode());


        System.err.println("unicornio header "+ headers);

        BannerInfoDTO[] banners = rest.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            BannerInfoDTO[].class
        ).getBody();

        assert banners != null;

    }


    @Test
    public void testGetABanner() throws IOException, java.io.IOException {
        
        UserDTO user = new UserDTO();
        user.setEmail("admin@example.com");
        user.setPassword("password123");

        //hacer el post del log in
        ResponseEntity<String> response = rest.postForEntity(SERVER_URL + "/login", user, String.class);

        System.err.println(response.getBody());
        rest.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + response.getBody());
            return execution.execute(request, body);
        }));

        // Crear el objeto de solicitud con el token JWT en el encabezado de autorización
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(response.getBody());
        
        String url = "http://localhost:8090/banner/1";
        
        //String url = SERVER_URL + "/banner/1";

        BannerInfoDTO banner = rest.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            BannerInfoDTO.class
        ).getBody();

        assert banner != null;
    }


    @Test
    public void testDeleteABanner() throws IOException, java.io.IOException {

        //testCreateABannerFunc();
        UserDTO user = new UserDTO();
        user.setEmail("admin@example.com");
        user.setPassword("password123");

        //hacer el post del log in
        ResponseEntity<String> response = rest.postForEntity(SERVER_URL + "/login", user, String.class);

        System.err.println(response.getBody());
        rest.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + response.getBody());
            return execution.execute(request, body);
        }));

        // Crear el objeto de solicitud con el token JWT en el encabezado de autorización
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(response.getBody());
        
        String url = "http://localhost:8090/banner/1";

        //String url = SERVER_URL + "/banner/1";

        String banner = rest.exchange(
            url,
            HttpMethod.DELETE,
            new HttpEntity<>(headers),
            String.class
        ).getBody();

        assertEquals("Banner deleted", banner);
    }


    @Test
    public void testEditABanner() throws java.io.IOException {
        
        //testCreateABannerFunc();

        // Crear archivo de prueba
        MockMultipartFile mockFile = new MockMultipartFile(
                "picture",  // El nombre debe coincidir con el nombre del campo en el controlador
                "test-image.jpg",
                "image/jpeg",
                "Test Image Content".getBytes()
        );

        // Crear los parámetros del cuerpo de la solicitud como MultiValueMap
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", "prueba titulo NO 2");  // Añadir el título del banner
        body.add("adminId", "1");  // Añadir ID del empleado administrativo
        body.add("picture", new ByteArrayResource(mockFile.getBytes()) {  // Añadir el archivo
            @Override
            public String getFilename() {
                return mockFile.getOriginalFilename();  // Definir el nombre del archivo
            }
        });

        // Establecer headers para multipart/form-data
        //headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        System.err.println("REVISAR BIEN "+headers);

        // Crear la entidad de la solicitud
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Enviar la solicitud usando TestRestTemplate
        ResponseEntity<?> response = rest.exchange(
            SERVER_URL + "/banner/1",
            HttpMethod.PUT,
            requestEntity,
            Banner.class
        );

        // Verificar la respuesta (aserciones)
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

   
    @Test
    public void testCreateABannerWithRepeatedName() throws java.io.IOException {
        
    MockMultipartFile mockFile = new MockMultipartFile(
            "picture",  // El nombre debe coincidir con el nombre del campo en el controlador
            "test-image.jpg",
            "image/jpeg",
            "Test Image Content".getBytes()
    );

    // Crear los parámetros del cuerpo de la solicitud como MultiValueMap
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("title", "prueba titulo");  // Añadir el título del banner
    body.add("adminId", "1");  // Añadir ID del empleado administrativo
    body.add("picture", new ByteArrayResource(mockFile.getBytes()) {  // Añadir el archivo
        @Override
        public String getFilename() {
            return mockFile.getOriginalFilename();  // Definir el nombre del archivo
        }
    });

    // Establecer headers para multipart/form-data
    //headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    System.err.println("REVISAR BIEN "+headers);

    // Crear la entidad de la solicitud
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    // Enviar la solicitud usando TestRestTemplate
    ResponseEntity<?> response = rest.exchange(
        SERVER_URL + "/banner/new",
        HttpMethod.POST,
        requestEntity,
        String.class
    );

    // Verificar la respuesta (aserciones)
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        
    }

}
