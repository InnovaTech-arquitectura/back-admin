package com.innovatech.demo;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.innovatech.demo.Controller.PublicationController;
import com.innovatech.demo.DTO.BannerDTO;
import com.innovatech.demo.DTO.UserDTO;
import com.innovatech.demo.Entity.Banner;
import com.innovatech.demo.Service.PublicationService;
import com.innovatech.demo.Service.UserService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class PublicationControllerTest {

    private static final String SERVER_URL = "http://localhost:8090";

    @Autowired
    private PublicationController publicationController;
    
    @Autowired
    private TestRestTemplate rest;

    ResponseEntity<String> response=null;

    @BeforeEach
    public void setUp() {

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

    }

    @Test
    public void testCreateABanner() {
       
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "Test Image Content".getBytes()
        );

        BannerDTO bannerDTO = new BannerDTO("prueba titulo",mockFile, 1L);


    }

    @Test
    public void testGetAllBanners() {

    }

    @Test
    public void testGetABanner() {

    }

    @Test
    public void testDeleteABanner() {

    }


    @Test
    public void testEditABanner() {

    }

    @Test
    public void testCreateABannerWithRepeatedName() {

    }

}
