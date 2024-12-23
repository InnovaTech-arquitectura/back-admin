package com.innovatech.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Service.FunctionalityService;
import com.innovatech.demo.Service.PlanService;
import com.innovatech.demo.Service.SubscriptionService;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    @Autowired
    private FunctionalityService functionalityService;

    @Autowired
    private SubscriptionService subscriptionService;

    // http://localhost:8090/plan/all?limit=n&page=m
    @GetMapping("/all")
    public ResponseEntity<?> getAllPlans(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return ResponseEntity.ok(planService.findAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // http://localhost:8090/plan/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlanById(@PathVariable String id) {
        try {
            // Check if the ID is a number
            Long planId = Long.parseLong(id);

            // Find the plan by ID
            Plan plan = planService.findById(planId);

            // If the plan does not exist, return 404 Not Found
            if (plan == null) {
                return new ResponseEntity<>("Plan not found", HttpStatus.NOT_FOUND);
            }

            // If plan exist, return the plan with 200 OK
            return new ResponseEntity<>(plan, HttpStatus.OK);

        } catch (NumberFormatException e) {
            // If the ID is not a number, return 400 Bad Request
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
    }

    // http://localhost:8090/plan/functionality
    @GetMapping("/functionality")
    public ResponseEntity<?> getAllPlanFunctionalities() {
        try {
            List<Functionality> functionalities = functionalityService.findAll();

            if (functionalities.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No functionalities found");
            }

            return ResponseEntity.ok(functionalities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // http://localhost:8090/plan/add
    @PostMapping("/add")
    public ResponseEntity<?> addPlan(@RequestBody Plan plan) {
        try {
            // Check if the plan name already exists to avoid duplicates
            Plan existingPlan = planService.findByName(plan.getName());
            if (existingPlan != null) {
                // Return 409 Conflict if the plan already exists
                return new ResponseEntity<>("Conflict: Duplicate entry", HttpStatus.CONFLICT);
            }

            // Save the new plan
            Plan planDB = planService.save(plan);

            if (planDB == null) {
                // If the plan could not be saved, return 400 Bad Request
                return new ResponseEntity<>("Unable to add Plan", HttpStatus.BAD_REQUEST);
            }

            // Return 201 Created if the plan was saved successfully
            return new ResponseEntity<>(planDB, HttpStatus.CREATED);

        } catch (Exception e) {
            // Handle any other exception and return 400 Bad Request
            return new ResponseEntity<>("Unable to add Plan", HttpStatus.BAD_REQUEST);
        }
    }

    // http://localhost:8090/plan/update
    @PutMapping("/update")
    public ResponseEntity<?> updatePlan(@RequestBody Plan plan) {
        try {
            // Check if the plan exists
            Plan existingPlan = planService.findById(plan.getId());

            // If the plan does not exist, return 409 Conflict
            if (existingPlan == null) {
                return new ResponseEntity<>("Conflict: Plan not found", HttpStatus.NOT_FOUND);
            }

            planService.save(plan);

            // Return 201 Created if the update was successful
            return new ResponseEntity<>("Plan modified successfully", HttpStatus.CREATED);

        } catch (Exception e) {
            // Handle any other exception and return 400 Bad Request
            return new ResponseEntity<>("Unable to modify Plan", HttpStatus.BAD_REQUEST);
        }
    }

    // http://localhost:8090/plan/delete/{idPlan}
    // PlanController.java
    @DeleteMapping("/delete/{idPlan}")
    public ResponseEntity<?> deletePlan(@PathVariable Long idPlan) {
        try {
            // Verificar si el plan existe
            Plan existingPlan = planService.findById(idPlan);
            if (existingPlan == null) {
                return new ResponseEntity<>("Plan no encontrado", HttpStatus.NOT_FOUND);
            }

            // Verificar si el plan tiene suscripciones activas
            boolean hasActiveSubscriptions = subscriptionService.hasActiveSubscriptions(idPlan);
            if (hasActiveSubscriptions) {
                return new ResponseEntity<>("No se puede eliminar: El plan tiene suscripciones activas", HttpStatus.CONFLICT);
            }

            // Si no hay suscripciones activas, proceder a eliminar el plan
            planService.deleteById(idPlan);

            return new ResponseEntity<>("Plan eliminado correctamente", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("No se puede eliminar el plan. Revisa la peticion", HttpStatus.BAD_REQUEST);
        }
    }


}
