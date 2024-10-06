package com.innovatech.demo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Service.EntrepreneurshipService;
import com.innovatech.demo.Service.EventService;
import com.innovatech.demo.Service.PlanService;
import com.innovatech.demo.Service.SubscriptionService;

@RestController
@RequestMapping("/finance")
public class FinanzasController {

    @Autowired
    private PlanService planService;

    @Autowired
    private EntrepreneurshipService entrepreneurshipService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private EventService eventService;

    // Method to get all entrepreneurships with pagination
    // http://localhost:8090/finanzas/all?limit=n&page=m
    @GetMapping("/all")
    public ResponseEntity<?> getAllEntrepreneurshipsWithActivePlan() {
        try {
            List<Map<String, Object>> response = entrepreneurshipService.findAllWithActivePlan();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();  // Para depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }


    // http://localhost:8090/finance/suscriptionforplan
    @GetMapping("/suscriptionforplan")
    public ResponseEntity<List<Plan>> Subscriptionforplan() {
        return new ResponseEntity<>(planService.Subscriptionforplan(), HttpStatus.OK);
    }

    // Method to get the total income of all entrepreneurships
    // http://localhost:8090/finance/allIncome
    @GetMapping("/allIncome")
    public List<Object[]> IngresosTotalesEntrepreneurships() {
        return entrepreneurshipService.IngresosTotalesEntrepreneurships();
    }

    // Method to get the income by Month and Year
    // http://localhost:8090/finance/incomeByMonth?month=1&year=2021
    @GetMapping("/incomeByMonthYear")
    public ResponseEntity<Map<String, Object>> getIncomeByMonthAndYear(
            @RequestParam(value = "month", required = true) Integer month,
            @RequestParam(value = "year", required = true) Integer year) {

        // Check if the 'month' and 'year' parameters are provided
        if (month == null || year == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Both 'month' and 'year' parameters are required"));
        }

        // Call the service to get the income for the specific month and year
        Map<String, Object> incomeData = entrepreneurshipService.getIncomeForSpecificMonth(month, year);
        return ResponseEntity.ok(incomeData);
    }

    // Method to get the income by year
    // http://localhost:8090/finance/incomeByYear?year=2024
    @GetMapping("/incomeByYear")
    public ResponseEntity<Map<String, Object>> getIncomeByYear(
            @RequestParam(value = "year", required = true) Integer year) {

        // Check if the 'year' parameter is provided
        if (year == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "'year' parameter is required"));
        }

        // Call the service to get the income for the specific year
        Map<String, Object> incomeData = entrepreneurshipService.getIncomeByYear(year);
        return ResponseEntity.ok(incomeData);
    }

    //Method to get expenses by year
    // http://localhost:8090/finance/expensesByYear?year=2021
    @GetMapping("/expensesByYear")
    public ResponseEntity<Map<String, Object>> getExpensesByYear(
            @RequestParam(value = "year", required = true) Integer year) {

        // Validar que el parámetro 'year' sea proporcionado
        if (year == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "'year' parameter is required"));
        }

        // Llamar al servicio para obtener los gastos del año proporcionado
        Map<String, Object> expenseData = eventService.getExpensesByYear(year);
        return ResponseEntity.ok(expenseData);
    }
    

    // Method to get the number of plans subscribed by entrepreneurs per year
    // http://localhost:8090/finance/usersByPlan?year=2021
    @GetMapping("/usersByPlan")
    public ResponseEntity<Map<String, Object>> getUsersByPlan(@RequestParam("year") int year) {
        Map<String, Object> result = subscriptionService.getUsersByPlanForYear(year);
        return ResponseEntity.ok(result);
    }

    // Method to get the income of plans per year
    // http://localhost:8090/finance/incomeByPlan?year=2021
    @GetMapping("/incomeByPlan")
    public ResponseEntity<Map<String, Object>> getIncomeByPlan(@RequestParam("year") int year) {
        Map<String, Object> result = subscriptionService.getIncomeByPlanForYear(year);
        return ResponseEntity.ok(result);
    }
}


    

 
