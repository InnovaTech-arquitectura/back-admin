package com.innovatech.demo.Controller;

import com.innovatech.demo.DTO.CouponDTO;
import com.innovatech.demo.Entity.Coupon;
import com.innovatech.demo.Entity.CouponFunctionality;
import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Entity.PlanFunctionality;
import com.innovatech.demo.Repository.FunctionalityRepository;
import com.innovatech.demo.Repository.PlanFunctionalityRepository;
import com.innovatech.demo.Repository.PlanRepository;
import com.innovatech.demo.Repository.RepositoryEntrepreneurship;
import com.innovatech.demo.Service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

/**
 * Controlador para manejar las operaciones CRUD relacionadas con los cupones (Coupon).
 * Los cupones están vinculados a los emprendimientos y contienen detalles como
 * la descripción, el descuento, la fecha de vencimiento y el período de vencimiento.
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;
    private final RepositoryEntrepreneurship entrepreneurshipRepository;
    private final PlanFunctionalityRepository planFunctionalityRepository;
    private final PlanRepository planRepository;
    private final FunctionalityRepository functionalityRepository;
  

    /**
     * Inyección de dependencias mediante constructor.
     * 
     * @param couponService El servicio para manejar operaciones con cupones.
     * @param entrepreneurshipRepository El repositorio para manejar los emprendimientos.
     * @param planRepository El repositorio para manejar los planes.
     */
    @Autowired
    public CouponController(CouponService couponService,
                             RepositoryEntrepreneurship entrepreneurshipRepository,
                             PlanRepository planRepository,
                             PlanFunctionalityRepository planFunctionalityRepository,
                             FunctionalityRepository functionalityRepository) {
        this.couponService = couponService;
        this.entrepreneurshipRepository = entrepreneurshipRepository;
        this.planRepository = planRepository;
        this.planFunctionalityRepository = planFunctionalityRepository;
        this.functionalityRepository = functionalityRepository;
    }


    /**
     * Endpoint para obtener todos los cupones con soporte de paginación.
     * 
     * @return Una lista de cupones.
     */
   @GetMapping("/all")
    public ResponseEntity<?> getAllCoupons(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Coupon> coupons = couponService.findAll(pageable);
            return ResponseEntity.ok(coupons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while fetching the coupons.");
        }
    }
    /**
     * Endpoint para obtener un cupón específico por su ID.
     * 
     * @param id El ID del cupón que se desea obtener.
     * @return El cupón con el ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable Long id) {
        try {
            Coupon coupon = couponService.findById(id);
            if (coupon == null) {
                return new ResponseEntity<>("Coupon not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(coupon, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred while fetching the coupon.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para agregar un nuevo cupón.
     * 
     * @param couponDTO El objeto DTO del cupón que se quiere agregar.
     * @return El cupón creado o un mensaje de conflicto en caso de duplicación.
     */
    @PostMapping("/add")
public ResponseEntity<?> addCoupon(@RequestBody CouponDTO couponDTO) {
    try {
        // Verificar que el emprendimiento asociado exista
        Entrepreneurship entrepreneurship = entrepreneurshipRepository.findById(couponDTO.getEntrepreneurshipId())
                .orElseThrow(() -> new RuntimeException("Entrepreneurship not found with ID: " + couponDTO.getEntrepreneurshipId()));

        // Verificar si ya existe un cupón con la misma descripción
        Coupon existingCoupon = couponService.findByDescription(couponDTO.getDescription());
        if (existingCoupon != null) {
            return new ResponseEntity<>("Conflict: A coupon with this description already exists.", HttpStatus.CONFLICT);
        }

        // Crear el nuevo cupón
        Coupon newCoupon = Coupon.builder()
                .description(couponDTO.getDescription())
                .expirationDate(couponDTO.getExpirationDate())
                .expirationPeriod(couponDTO.getExpirationPeriod())
                .entrepreneurship(entrepreneurship)
                .build();

        // Verificar si hay un plan asociado
        if (couponDTO.getPlanId() != null) {
            Plan plan = planRepository.findById(couponDTO.getPlanId())
                    .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + couponDTO.getPlanId()));

            // Asignar el plan al cupón
            newCoupon.setPlan(plan);

            // Asociar funcionalidades al plan si se proporcionaron
            if (couponDTO.getFunctionalityIds() != null && !couponDTO.getFunctionalityIds().isEmpty()) {
                for (Long functionalityId : couponDTO.getFunctionalityIds()) {
                    Functionality functionality = functionalityRepository.findById(functionalityId)
                            .orElseThrow(() -> new RuntimeException("Functionality not found with ID: " + functionalityId));

                    // Crear la relación entre el cupón y la funcionalidad
                    CouponFunctionality couponFunctionality = new CouponFunctionality(newCoupon, functionality);
                    newCoupon.getCouponFunctionalities().add(couponFunctionality);

                    // Guardar la relación plan-funcionalidad
                    plan.addFunctionality(functionality);
                }
            }
        }

        // Guardar el cupón
        Coupon savedCoupon = couponService.save(newCoupon);
        return new ResponseEntity<>(savedCoupon, HttpStatus.CREATED);

    } catch (RuntimeException e) {
        e.printStackTrace();
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("Unable to create coupon. Please check the request.", HttpStatus.BAD_REQUEST);
    }
}

    
    
    /**
     * Endpoint para actualizar un cupón existente.
     * 
     * @param couponDTO El objeto DTO del cupón con los datos actualizados.
     * @return Un mensaje de éxito si la actualización fue exitosa.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateCoupon(@RequestBody CouponDTO couponDTO) {
        try {
            // Buscar el cupón existente
            Coupon existingCoupon = couponService.findById(couponDTO.getId());
            if (existingCoupon == null) {
                return new ResponseEntity<>("Coupon not found", HttpStatus.NOT_FOUND);
            }

            // Verificar que el emprendimiento asociado exista
            Entrepreneurship entrepreneurship = entrepreneurshipRepository.findById(couponDTO.getEntrepreneurshipId())
                    .orElseThrow(() -> new RuntimeException("Entrepreneurship not found"));

            // Verificar que el plan asociado exista
            Plan plan = planRepository.findById(couponDTO.getPlanId())
                    .orElseThrow(() -> new RuntimeException("Plan not found"));

            // Actualizar el cupón existente
            existingCoupon.setDescription(couponDTO.getDescription());
            existingCoupon.setExpirationDate(couponDTO.getExpirationDate());
            existingCoupon.setExpirationPeriod(couponDTO.getExpirationPeriod());
            existingCoupon.setEntrepreneurship(entrepreneurship);
            existingCoupon.setPlan(plan);

            // Guardar el cupón actualizado
            couponService.save(existingCoupon);
            return new ResponseEntity<>("Coupon updated successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to update coupon. Please check the request.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint para eliminar un cupón por su ID.
     * 
     * @param id El ID del cupón que se quiere eliminar.
     * @return Un mensaje de éxito si el cupón fue eliminado.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        try {
            if (!couponService.existsById(id)) {
                return new ResponseEntity<>("Coupon not found", HttpStatus.NOT_FOUND);
            }

            couponService.deleteById(id);
            return new ResponseEntity<>("Coupon deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to delete the coupon. Please check the request and try again.", HttpStatus.BAD_REQUEST);
        }
    }
}
