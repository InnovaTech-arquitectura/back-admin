package com.innovatech.demo;

import com.innovatech.demo.Controller.FinanzasController;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Service.EntrepreneurshipService;
import com.innovatech.demo.Service.EventService;
import com.innovatech.demo.Service.PlanService;
import com.innovatech.demo.Service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinanzasControllerTest {

    @InjectMocks
    private FinanzasController finanzasController;

    @Mock
    private PlanService planService;

    @Mock
    private EntrepreneurshipService entrepreneurshipService;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEntrepreneurshipsWithActivePlan() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        when(entrepreneurshipService.findAllWithActivePlan()).thenReturn(mockResponse);

        ResponseEntity<?> response = finanzasController.getAllEntrepreneurshipsWithActivePlan();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(entrepreneurshipService, times(1)).findAllWithActivePlan();
    }

    @Test
    void testSubscriptionForPlan() {
        List<Plan> mockPlans = new ArrayList<>();
        when(planService.Subscriptionforplan()).thenReturn(mockPlans);

        ResponseEntity<List<Plan>> response = finanzasController.Subscriptionforplan();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPlans, response.getBody());
        verify(planService, times(1)).Subscriptionforplan();
    }

    @Test
    void testIngresosTotalesEntrepreneurships() {
        List<Object[]> mockIncome = new ArrayList<>();
        when(entrepreneurshipService.IngresosTotalesEntrepreneurships()).thenReturn(mockIncome);

        List<Object[]> response = finanzasController.IngresosTotalesEntrepreneurships();

        assertEquals(mockIncome, response);
        verify(entrepreneurshipService, times(1)).IngresosTotalesEntrepreneurships();
    }

    @Test
    void testGetIncomeByMonthAndYear() {
        Map<String, Object> mockIncomeData = new HashMap<>();
        when(entrepreneurshipService.getIncomeForSpecificMonth(1, 2021)).thenReturn(mockIncomeData);

        ResponseEntity<Map<String, Object>> response = finanzasController.getIncomeByMonthAndYear(1, 2021);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockIncomeData, response.getBody());
        verify(entrepreneurshipService, times(1)).getIncomeForSpecificMonth(1, 2021);
    }


    @Test
    void testGetIncomeByYear() {
        Map<String, Object> mockIncomeData = new HashMap<>();
        when(entrepreneurshipService.getIncomeByYear(2024)).thenReturn(mockIncomeData);

        ResponseEntity<Map<String, Object>> response = finanzasController.getIncomeByYear(2024);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockIncomeData, response.getBody());
        verify(entrepreneurshipService, times(1)).getIncomeByYear(2024);
    }


    @Test
    void testGetExpensesByYear() {
        Map<String, Object> mockExpenseData = new HashMap<>();
        when(eventService.getExpensesByYear(2021)).thenReturn(mockExpenseData);

        ResponseEntity<Map<String, Object>> response = finanzasController.getExpensesByYear(2021);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockExpenseData, response.getBody());
        verify(eventService, times(1)).getExpensesByYear(2021);
    }

    @Test
    void testGetUsersByPlan() {
        Map<String, Object> mockResult = new HashMap<>();
        when(subscriptionService.getUsersByPlanForYear(2021)).thenReturn(mockResult);

        ResponseEntity<Map<String, Object>> response = finanzasController.getUsersByPlan(2021);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResult, response.getBody());
        verify(subscriptionService, times(1)).getUsersByPlanForYear(2021);
    }

    @Test
    void testGetIncomeByPlan() {
        Map<String, Object> mockResult = new HashMap<>();
        when(subscriptionService.getIncomeByPlanForYear(2021)).thenReturn(mockResult);

        ResponseEntity<Map<String, Object>> response = finanzasController.getIncomeByPlan(2021);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResult, response.getBody());
        verify(subscriptionService, times(1)).getIncomeByPlanForYear(2021);
    }

    
}
