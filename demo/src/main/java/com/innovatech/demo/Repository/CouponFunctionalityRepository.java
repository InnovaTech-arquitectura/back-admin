package com.innovatech.demo.Repository;

import com.innovatech.demo.Entity.CouponFunctionality;

import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponFunctionalityRepository extends JpaRepository<CouponFunctionality, Long> {
    // Puedes agregar más métodos personalizados si necesitas consultar por funcionalidades de cupones específicas
   
}
