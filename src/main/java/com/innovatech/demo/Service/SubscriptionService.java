package com.innovatech.demo.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Repository.SubscriptionRepository;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Map<String, Object> getUsersByPlanForYear(int year) {
        List<Object[]> results = subscriptionRepository.countUsersByPlanForYear(year);

        // Inicializar las listas para las etiquetas (labels) y los datos (data)
        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        // Procesar los resultados
        for (Object[] result : results) {
            String planName = (String) result[1];  // El nombre del plan
            Long userCount = (Long) result[2];     // El número de usuarios

            // Agregar los valores a las listas
            labels.add(planName);
            data.add(userCount.intValue());
        }

        // Preparar el resultado en formato JSON
        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);
        response.put("label", "Usuarios por plan en " + year);

        return response;
    }

    public Map<String, Object> getIncomeByPlanForYear(int year) {
        List<Object[]> results = subscriptionRepository.sumIncomeByPlanForYear(year);

        // Inicializar las listas para las etiquetas (labels) y los datos (data)
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        // Procesar los resultados
        for (Object[] result : results) {
            String planName = (String) result[1];  // El nombre del plan
            Double totalIncome = (Double) result[2];  // El ingreso total para el plan

            // Agregar los valores a las listas
            labels.add(planName);
            data.add(totalIncome);
        }

        // Preparar el resultado en formato JSON
        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);
        response.put("label", "Ingresos por plan en " + year);

        return response;
    }

    // Method to check if a plan has active subscriptions
    public boolean hasActiveSubscriptions(Long planId) {
        return subscriptionRepository.existsByPlanIdAndExpirationDateAfter(planId, new Date(System.currentTimeMillis()));
    }
}

