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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(ProfileController.class)
@ContextConfiguration(classes = ProfileController.class) // Asegura cargar solo el controlador
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private AdministrativeEmployeeService administrativeEmployeeService;

    @Test
    void createProfile_ShouldReturnCreatedProfile() throws Exception {
        // Arrange
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

        // Mocking services
        when(roleService.existsByName(profileDTO.getRole())).thenReturn(true);
        when(roleService.findByName(profileDTO.getRole())).thenReturn(Optional.of(role));
        when(userService.save(any(UserEntity.class))).thenReturn(userEntity);
        when(administrativeEmployeeService.save(any(AdministrativeEmployee.class))).thenReturn(administrativeEmployee);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCard\":123456,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"password\":\"securepassword\",\"role\":\"ADMIN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.name", is("John Doe")))
                .andExpect(jsonPath("$.user.email", is("john.doe@example.com")));
    }

    @Test
    void getAllProfiles_ShouldReturnProfiles() throws Exception {
        // Arrange
        AdministrativeEmployee employee = new AdministrativeEmployee();
        Page<AdministrativeEmployee> page = new PageImpl<>(List.of(employee));
        Mockito.when(administrativeEmployeeService.findAll(any(Pageable.class)))
               .thenReturn(page);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/profile/all/10/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(1))); 
    }

    @Test
    void getProfileById_ShouldReturnProfile() throws Exception {
        // Arrange
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
    
        // Mocking the service
        when(administrativeEmployeeService.findById(profileId)).thenReturn(administrativeEmployee);
    
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/profile/{id}", profileId)
                       .header("X-Test-Scenario", "getProfileById")) 
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.user.name", is("John Doe")))
               .andExpect(jsonPath("$.user.email", is("john.doe@example.com")));
    }

    @Test
    void getProfileByRole_ShouldReturnProfiles() throws Exception {
        // Arrange
        Long idRole = 1L;
        AdministrativeEmployee employee = new AdministrativeEmployee();
        Mockito.when(roleService.existsById(idRole)).thenReturn(true);
        Mockito.when(administrativeEmployeeService.findByRoleId(idRole)).thenReturn(List.of(employee));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/profile/role/{idRole}", idRole))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Get the list of administrative profiles with a specific role"));
    }

    @Test
    void updateProfile_ShouldUpdateAndReturnUpdatedProfile() throws Exception {
        // Arrange
        Long adminId = 1L;
        UserEntity existingUser = UserEntity.builder()
                .id(1L)  // Asegúrate de que el ID no sea nulo
                .idCard(123456)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("securepassword")
                .build();

        AdministrativeEmployee existingAdmin = AdministrativeEmployee.builder()
                .id(adminId)
                .user(existingUser)
                .build();

        UserEntity updatedUser = UserEntity.builder()
                .id(1L)  // Asegúrate de que el ID no sea nulo
                .idCard(123456)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .password("newsecurepassword")
                .build();

        AdministrativeEmployee updatedAdmin = AdministrativeEmployee.builder()
                .id(adminId)
                .user(updatedUser)
                .build();

        // Mocking services
        when(administrativeEmployeeService.findById(adminId)).thenReturn(existingAdmin);
        when(userService.save(any(UserEntity.class))).thenReturn(updatedUser);
        when(administrativeEmployeeService.save(any(AdministrativeEmployee.class))).thenReturn(updatedAdmin);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"user\":{\"id\":1,\"idCard\":123456,\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\",\"password\":\"newsecurepassword\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.name", is("Jane Doe")))
                .andExpect(jsonPath("$.user.email", is("jane.doe@example.com")));
    }

    @Test
    void deleteProfile_ShouldDeleteAndReturnConfirmation() throws Exception {
        // Arrange
        Long adminId = 1L;

        // Mocking the service
        when(administrativeEmployeeService.existsById(adminId)).thenReturn(true);
        Mockito.doNothing().when(administrativeEmployeeService).deleteById(adminId);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/profile/{id}", adminId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Profile deleted"));
    }
}
