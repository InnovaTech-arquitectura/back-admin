package com.innovatech.demo.Repository;
import java.util.Optional;
import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministrativeEmployeeRepository extends JpaRepository<AdministrativeEmployee, Long> {
    // Query for finding all administrative employees by their user role
    @Query("SELECT a FROM AdministrativeEmployee a WHERE a.user.role.id = :roleId")
    List<AdministrativeEmployee> findByRoleId(Long roleId);

    Optional<AdministrativeEmployee> findByUser(UserEntity user);

}
