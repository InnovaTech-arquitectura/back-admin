package com.innovatech.demo;

import com.innovatech.demo.Controller.ProfileController;
import com.innovatech.demo.DTO.ProfileDTO;
import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Service.AdministrativeEmployeeService;
import com.innovatech.demo.Service.RoleService;
import com.innovatech.demo.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.security.Key;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

//For run all the test use the command: mvn test -Dtest=ProfileControllerTest
@WebMvcTest(ProfileController.class)
@ContextConfiguration(classes = {ProfileController.class})
@AutoConfigureMockMvc(addFilters = false) 
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private AdministrativeEmployeeService administrativeEmployeeService;

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private String generateTestJWT() {
        return Jwts.builder()
                .setSubject("testuser@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    //For run this test use the command: mvn -Dtest=ProfileControllerTest#testCreateProfile_ShouldReturnCreatedProfile test
    @Test
    void createProfile_ShouldReturnCreatedProfile() throws Exception {
        String jwtToken = "Bearer " + generateTestJWT();

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setIdCard(123456);
        profileDTO.setName("John Doe");
        profileDTO.setEmail("john.doe@example.com");
        profileDTO.setPassword("securepassword");
        profileDTO.setRole("ADMIN");

        Role role = new Role();
        role.setName("ADMIN");

        UserEntity userEntity = UserEntity.builder()
                .idCard(profileDTO.getIdCard())
                .name(profileDTO.getName())
                .email(profileDTO.getEmail())
                .password(profileDTO.getPassword())
                .role(role)
                .build();

        AdministrativeEmployee administrativeEmployee = AdministrativeEmployee.builder()
                .user(userEntity)
                .build();

        when(roleService.existsByName(profileDTO.getRole())).thenReturn(true);
        when(roleService.findByName(profileDTO.getRole())).thenReturn(Optional.of(role));
        when(userService.save(any(UserEntity.class))).thenReturn(userEntity);
        when(administrativeEmployeeService.save(any(AdministrativeEmployee.class))).thenReturn(administrativeEmployee);

        mockMvc.perform(MockMvcRequestBuilders.post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken)
                        .content("{\"idCard\":123456,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"password\":\"securepassword\",\"role\":\"ADMIN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.name", is("John Doe")))
                .andExpect(jsonPath("$.user.email", is("john.doe@example.com")));
    }

    //For run this test use the command: mvn -Dtest=ProfileControllerTest#testCreateProfile_ShouldReturnBadRequest test
    @Test
    void getAllProfiles_ShouldReturnProfiles() throws Exception {
        String jwtToken = "Bearer " + generateTestJWT();

        AdministrativeEmployee employee = new AdministrativeEmployee();
        Page<AdministrativeEmployee> page = new PageImpl<>(List.of(employee));
        Mockito.when(administrativeEmployeeService.findAll(any(Pageable.class)))
                .thenReturn(page);


        mockMvc.perform(MockMvcRequestBuilders.get("/profile/all/10/1")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    //For run this test use the command: mvn -Dtest=ProfileControllerTest#testCreateProfile_ShouldReturnBadRequest test
    @Test
    void getProfileById_ShouldReturnProfile() throws Exception {

        String jwtToken = "Bearer " + generateTestJWT();
        Long profileId = 1L;

        UserEntity userEntity = UserEntity.builder()
                .idCard(123456)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("securepassword")
                .build();

        AdministrativeEmployee administrativeEmployee = AdministrativeEmployee.builder()
                .user(userEntity)
                .build();

        when(administrativeEmployeeService.findById(profileId)).thenReturn(administrativeEmployee);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/{id}", profileId)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.name", is("John Doe")))
                .andExpect(jsonPath("$.user.email", is("john.doe@example.com")));
    }

    //For run this test use the command: mvn -Dtest=ProfileControllerTest#testCreateProfile_ShouldReturnBadRequest test
    @Test
    @WithMockUser(roles = "ADMIN")
    void getProfileByRole_ShouldReturnProfiles() throws Exception {
        Long idRole = 1L;

        AdministrativeEmployee employee = new AdministrativeEmployee();
        Mockito.when(roleService.existsById(idRole)).thenReturn(true);
        Mockito.when(administrativeEmployeeService.findByRoleId(idRole)).thenReturn(List.of(employee));

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/role/{idRole}", idRole))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(employee.getId())))
                .andExpect(jsonPath("$[0].user", is(employee.getUser())));
    }


    //For run this test use the command: mvn -Dtest=ProfileControllerTest#testCreateProfile_ShouldReturnBadRequest test
    @Test
    void deleteProfile_ShouldDeleteAndReturnConfirmation() throws Exception {
        String jwtToken = "Bearer " + generateTestJWT();
        Long adminId = 1L;

        when(administrativeEmployeeService.existsById(adminId)).thenReturn(true);
        Mockito.doNothing().when(administrativeEmployeeService).deleteById(adminId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/profile/{id}", adminId)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Profile deleted"));
    }
}
