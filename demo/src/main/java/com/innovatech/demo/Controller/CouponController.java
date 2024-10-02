package com.innovatech.demo.Controller;

import com.innovatech.demo.Entity.Coupon;
import com.innovatech.demo.Service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar las operaciones CRUD relacionadas con los cupones (Coupon).
 * Los cupones están vinculados a los emprendimientos y contienen detalles como
 * la descripción, el descuento, la fecha de vencimiento y el período de vencimiento.
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    /**
     * Inyección de dependencias mediante constructor.
     * 
     * @param couponService El servicio para manejar operaciones con cupones.
     */
    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    /**
     * Endpoint para obtener todos los cupones con soporte de paginación.
     * 
     * @param limit El número máximo de cupones que se devolverán por página.
     * @param page  El número de página para la paginación.
     * @return Una lista paginada de cupones.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllCoupons(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return ResponseEntity.ok(couponService.findAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while fetching the coupons.");
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
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid parameters. The ID must be a valid number.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred while fetching the coupon.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para agregar un nuevo cupón. Se verifica si ya existe un cupón con la misma descripción
     * antes de guardarlo para evitar duplicados.
     * 
     * @param coupon El objeto cupón que se quiere agregar.
     * @return El cupón creado o un mensaje de conflicto en caso de duplicación.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon) {
        try {
            // Verificar si ya existe un cupón con la misma descripción
            Coupon existingCoupon = couponService.findByDescription(coupon.getDescription());
            if (existingCoupon != null) {
                return new ResponseEntity<>("Conflict: A coupon with this description already exists.", HttpStatus.CONFLICT);
            }

            // Verificar que el emprendimiento asociado exista
            if (coupon.getEntrepreneurship() == null || coupon.getEntrepreneurship().getId() == null) {
                return new ResponseEntity<>("Invalid request: The coupon must be associated with a valid entrepreneurship.", HttpStatus.BAD_REQUEST);
            }

            // Guardar el nuevo cupón
            Coupon savedCoupon = couponService.save(coupon);
            return new ResponseEntity<>(savedCoupon, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to add the coupon. Please check the request and try again.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint para actualizar un cupón existente.
     * 
     * @param coupon El objeto cupón con los datos actualizados.
     * @return Un mensaje de éxito si la actualización fue exitosa.
     */
    @PutMapping("/update")
public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon) {
    try {
        // Verificar si el cupón existe antes de actualizarlo
        if (!couponService.existsById(coupon.getId())) {
            return new ResponseEntity<>("El cupón que intentas actualizar no fue encontrado.", HttpStatus.NOT_FOUND);
        }

        // Validar que el emprendimiento asociado existe
        if (coupon.getEntrepreneurship() == null || coupon.getEntrepreneurship().getId() == null) {
            return new ResponseEntity<>("El emprendimiento asociado no es válido.", HttpStatus.BAD_REQUEST);
        }

        // Actualizar el cupón
        Coupon updatedCoupon = couponService.save(coupon);
        return new ResponseEntity<>(updatedCoupon, HttpStatus.OK);

    } catch (Exception e) {
        // Imprimir el error en los logs del servidor
        e.printStackTrace();
        return new ResponseEntity<>("No se pudo actualizar el cupón. Por favor, revisa los detalles y vuelve a intentarlo.", HttpStatus.BAD_REQUEST);
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
