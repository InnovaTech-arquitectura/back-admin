package com.innovatech.demo.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.demo.DTO.ProfileDTO;
import com.innovatech.demo.DTO.RoleDTO;
import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Service.AdministrativeEmployeeService;
import com.innovatech.demo.Service.RoleService;
import com.innovatech.demo.Service.UserService;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://10.43.100.240:4200/")
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdministrativeEmployeeService administrativeEmployeeService;

    @PostMapping()
    public ResponseEntity<?> createProfile(@RequestBody ProfileDTO profileDTO) {
        try {
            if (profileDTO == null) {
                return ResponseEntity.badRequest().body("Invalid parameters");
            }
            if (!roleService.existsByName(profileDTO.getRole())) {
                return ResponseEntity.badRequest().body("Role not found");
            }
            if (userService.existsByIdCard(profileDTO.getIdCard())) {
                return ResponseEntity.badRequest().body("Id card already exists");
            }
            if (userService.existsByEmail(profileDTO.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }
            Role role = roleService.findByName(profileDTO.getRole()).get();
            UserEntity userEntity = UserEntity.builder()
                    .idCard(profileDTO.getIdCard())
                    .name(profileDTO.getName())
                    .email(profileDTO.getEmail())
                    .password(profileDTO.getPassword())
                    .role(role)
                    .build();
            UserEntity savedUser = userService.save(userEntity);
            AdministrativeEmployee administrativeEmployee = AdministrativeEmployee.builder()
                    .user(savedUser)
                    .build();
            return ResponseEntity.ok(administrativeEmployeeService.save(administrativeEmployee));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error ocurred");
        }
    }

    @GetMapping("/all/{limit}/{pag}")
    public ResponseEntity<?> getAllProfiles(@PathVariable int limit, @PathVariable int pag) {
        try {
            if (limit <= 0 || pag <= 0) {
                return ResponseEntity.badRequest().body("Invalid parameters");
            }
            Pageable pageable = PageRequest.of(pag - 1, limit);
            return ResponseEntity.ok(administrativeEmployeeService.findAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error ocurred");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
            }
            AdministrativeEmployee administrativeEmployee = administrativeEmployeeService.findById(id);
            if (administrativeEmployee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
            }
            return ResponseEntity.ok(administrativeEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("role/{idRole}")
    public ResponseEntity<?> getProfileByRole(@PathVariable Long idRole) {
        try {
            if (idRole == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
            }
            if (!roleService.existsById(idRole)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
            }
            List<AdministrativeEmployee> users = administrativeEmployeeService.findByRoleId(idRole);
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrative employees not found");
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody AdministrativeEmployee newAdministrativeEmployee) {
        try {

            if (newAdministrativeEmployee == null || newAdministrativeEmployee.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
            }

            AdministrativeEmployee existingAdmin = administrativeEmployeeService
                    .findById(newAdministrativeEmployee.getId());
            if (existingAdmin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
            }

            UserEntity userToUpdate = newAdministrativeEmployee.getUser();
            if (userToUpdate == null || userToUpdate.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User data is missing or incomplete");
            }

            if (userToUpdate.getName() == null || userToUpdate.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Name cannot be empty");
            }
            if (userToUpdate.getEmail() == null || userToUpdate.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Email cannot be empty");
            }
            if (userToUpdate.getPassword() == null || userToUpdate.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Password cannot be empty");
            }

            if (userToUpdate.getRole() == null || userToUpdate.getRole().getId() == null) {
                Role existingRole = existingAdmin.getUser().getRole();
                if (existingRole == null) {
                    return ResponseEntity.badRequest().body("Role cannot be null");
                }
                userToUpdate.setRole(existingRole);
            }

            userToUpdate = userService.save(userToUpdate);
            existingAdmin.setUser(userToUpdate);

            administrativeEmployeeService.save(existingAdmin);

            return ResponseEntity.ok("Profile updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
            }
            if (!administrativeEmployeeService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
            }
            administrativeEmployeeService.deleteById(id);
            return ResponseEntity.ok("Profile deleted");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("role/all")
    public ResponseEntity<?> getAllRoles() {
        try {
            List<Role> roles = roleService.findAll();
            if (roles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Roles not found");
            }

            List<RoleDTO> rolesDTO = new ArrayList<>();
            for (Role role : roles) {
                long userCount = userService.countByRoleName(role.getName());

                RoleDTO roleDTO = RoleDTO.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .quantity(userCount)
                        .build();

                rolesDTO.add(roleDTO);
            }

            return ResponseEntity.ok(rolesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

}
