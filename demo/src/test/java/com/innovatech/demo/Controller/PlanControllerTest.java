package com.innovatech.demo.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.innovatech.demo.Controller.PlanController;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Service.PlanService;

public class PlanControllerTest {

    @InjectMocks
    private PlanController planController;

    @Mock
    private PlanService planService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPlans() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Plan plan = new Plan();
        plan.setId(1L);
        plan.setName("Plan A");
        when(planService.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(plan)));

        // Act
        ResponseEntity<?> response = planController.getAllPlans(10, 0);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetPlanById_Success() {
        // Arrange
        Long planId = 1L;
        Plan plan = new Plan();
        plan.setId(planId);
        plan.setName("Plan A");
        when(planService.findById(planId)).thenReturn(plan);

        // Act
        ResponseEntity<?> response = planController.getPlanById(String.valueOf(planId));

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plan, response.getBody());
    }

    @Test
    public void testGetPlanById_NotFound() {
        // Arrange
        Long planId = 1L;
        when(planService.findById(planId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = planController.getPlanById(String.valueOf(planId));

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Plan not found", response.getBody());
    }

    @Test
    public void testAddPlan_Success() {
        // Arrange
        Plan plan = new Plan();
        plan.setName("Plan A");
        when(planService.findByName(plan.getName())).thenReturn(null);
        when(planService.save(any(Plan.class))).thenReturn(plan);

        // Act
        ResponseEntity<?> response = planController.addPlan(plan);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(plan, response.getBody());
    }

    @Test
    public void testAddPlan_Conflict() {
        // Arrange
        Plan plan = new Plan();
        plan.setName("Plan A");
        when(planService.findByName(plan.getName())).thenReturn(new Plan());

        // Act
        ResponseEntity<?> response = planController.addPlan(plan);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict: Duplicate entry", response.getBody());
    }

    @Test
    public void testUpdatePlan_Success() {
        // Arrange
        Plan existingPlan = new Plan();
        existingPlan.setId(1L);
        when(planService.findById(existingPlan.getId())).thenReturn(existingPlan);
        when(planService.save(any(Plan.class))).thenReturn(existingPlan);

        // Act
        ResponseEntity<?> response = planController.updatePlan(existingPlan);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Plan modified successfully", response.getBody());
    }

    @Test
    public void testUpdatePlan_NotFound() {
        // Arrange
        Plan plan = new Plan();
        plan.setId(1L);
        when(planService.findById(plan.getId())).thenReturn(null);

        // Act
        ResponseEntity<?> response = planController.updatePlan(plan);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Conflict: Plan not found", response.getBody());
    }

    @Test
    public void testDeletePlan_Success() {
        // Arrange
        Long planId = 1L;
        Plan plan = new Plan();
        plan.setId(planId);
        when(planService.findById(planId)).thenReturn(plan);

        // Act
        ResponseEntity<?> response = planController.deletePlan(planId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Plan deleted successfully", response.getBody());
    }

    @Test
    public void testDeletePlan_NotFound() {
        // Arrange
        Long planId = 1L;

        when(planService.findById(planId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = planController.deletePlan(planId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Plan not found", response.getBody());
    }
}
