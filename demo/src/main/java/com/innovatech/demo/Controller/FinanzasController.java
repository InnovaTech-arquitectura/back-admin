package com.innovatech.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.innovatech.demo.Entity.Entrepreneurship;

import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Service.EntrepreneurshipService;
import com.innovatech.demo.Service.PlanService;

@RestController
@RequestMapping("/finanzas")
public class FinanzasController {
    
    @Autowired
    private PlanService planService;

    @Autowired
    private EntrepreneurshipService entrepreneurshipService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllEnrepreneurships() {

        return new ResponseEntity<>(entrepreneurshipService.findAllEntrepreneurships(), HttpStatus.OK);
    }

    @GetMapping("/suscriptionforplan")
    public ResponseEntity<List<Plan>> Subscriptionforplan() {
        return new ResponseEntity<>(planService.Subscriptionforplan(), HttpStatus.OK);
    }

    @GetMapping("/ingresosTotales")
    public ResponseEntity<List<Entrepreneurship>> IngresosTotalesEntrepreneurships() {
        return new ResponseEntity<>(entrepreneurshipService.IngresosTotalesEntrepreneurships(), HttpStatus.OK);
    }

   
}
