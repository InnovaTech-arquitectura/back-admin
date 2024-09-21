package com.innovatech.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Service.PlanService;

@RestController
@RequestMapping("plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    // TODO: Try and find the plan and its functionalities, right now it doesn't
    // return the functionalities associated with the plan
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

    // http://localhost:8090/plan/1
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlanById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(planService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // http://localhost:8090/plan/add
    @PostMapping("/add")
    public ResponseEntity<?> addPlan(@RequestBody Plan plan) {

        // TODO: Check if the plan already exists by name
        // if (planService.existsByName(plan.getName())) {
        // return new ResponseEntity<String>("Este plan ya existe",
        // HttpStatus.BAD_REQUEST);
        // }

        // Save the plan
        Plan planDB = planService.save(plan);

        if (planDB == null) {
            return new ResponseEntity<Plan>(planDB, HttpStatus.BAD_REQUEST);
        }

        // Return the saved plan with status 201 (CREATED)
        return new ResponseEntity<Plan>(planDB, HttpStatus.CREATED);
    }
}
