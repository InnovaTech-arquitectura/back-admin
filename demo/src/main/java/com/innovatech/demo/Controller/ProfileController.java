package com.innovatech.demo.Controller;

import com.innovatech.demo.DTO.ProfileDTO;
import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Service.RoleService;
import com.innovatech.demo.Service.UserService;
import org.h2.engine.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.innovatech.demo.Service.AdministrativeEmployeeService;

import java.util.List;

@RestController
@RequestMapping("/profile")
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
            if (roleService.existsByName(profileDTO.getRole())) {
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
            } else {
                return ResponseEntity.badRequest().body("Role not found");
            }
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
            Pageable pageable = PageRequest.of(pag, limit);
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


    @GetMapping("/{idRole}")
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
            return ResponseEntity.ok("Get the list of administrative profiles with a specific role");
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

            AdministrativeEmployee existingAdmin = administrativeEmployeeService.findById(newAdministrativeEmployee.getId());

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

            userToUpdate = userService.save(userToUpdate);
            existingAdmin.setUser(userToUpdate);

            return ResponseEntity.ok(administrativeEmployeeService.save(existingAdmin));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id){
        try{
            if(id == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
            }
            if(!administrativeEmployeeService.existsById(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
            }
            administrativeEmployeeService.deleteById(id);
            return ResponseEntity.ok("Profile deleted");

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
