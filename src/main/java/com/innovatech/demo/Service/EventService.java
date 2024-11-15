package com.innovatech.demo.Service;

import java.util.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Entrepreneurshipeventregistry;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Repository.EntrepreneurshipRepository;
import com.innovatech.demo.Repository.EntrepreneurshipeventregistryRepository;
import com.innovatech.demo.Repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService implements CrudService<EventEntity, Long> {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;
    @Autowired
    private EntrepreneurshipeventregistryRepository entrepreneurshipeventregistryRepository;

    @Override
    public EventEntity findById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    public Page<EventEntity> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public EventEntity findByName(String name) {
        return eventRepository.findByName(name);
    }

    @Override
    public EventEntity save(EventEntity eventEntity) {
    
        // Verifica si el evento tiene inscripciones
        List<Entrepreneurshipeventregistry> registrations = eventEntity.getEntrepreneurshipeventregistry();
    
        // Guardar las inscripciones de emprendedores
        for (Entrepreneurshipeventregistry registration : registrations) {
            if (registration.getEntrepreneurship() != null) {
                // Verifica que el emprendimiento existe
                Entrepreneurship entrepreneurship = entrepreneurshipRepository.findById(registration.getEntrepreneurship().getId())
                        .orElseThrow(() -> new RuntimeException("Entrepreneurship not found: " + registration.getEntrepreneurship().getId()));
                registration.setEntrepreneurship(entrepreneurship);
            }
    
            // Verifica si la inscripción ya tiene un ID
            if (registration.getId() != null) {
                // Busca el registro existente
                Entrepreneurshipeventregistry existingRegistration = entrepreneurshipeventregistryRepository.findById(registration.getId())
                        .orElseThrow(() -> new RuntimeException("Registration not found: " + registration.getId()));
                // Actualiza los campos necesarios
                existingRegistration.setAmountPaid(registration.getAmountPaid());
                existingRegistration.setDate(registration.getDate());
                entrepreneurshipeventregistryRepository.save(existingRegistration);
            } else {
                // Si no tiene ID, es una nueva inscripción
                entrepreneurshipeventregistryRepository.save(registration);
            }
        }
    
        // Guarda el evento
        return eventRepository.save(eventEntity);
    }
    

    public Map<String, Object> getExpensesByYear(int year) {
        List<Object[]> results = eventRepository.getAnnualExpensesByYear(year);
    
        // Inicializar el mapa con los meses
        Map<String, Object> response = new HashMap<>();
        String[] months = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                            "Octubre", "Noviembre", "Diciembre" };
        List<Integer> data = new ArrayList<>(Collections.nCopies(12, 0)); // Inicializa una lista de 12 ceros
    
        // Procesar los resultados
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
        for (Object[] result : results) {
            String dateString = (String) result[0]; // La fecha como String en formato 'yyyy-MM-dd'
            Double totalExpense = (Double) result[1]; // El total de gastos
    
            // Convertir el String en formato 'YYYY-MM-DD' a un mes usando LocalDate
            LocalDate date = LocalDate.parse(dateString, formatter);
            int monthResult = date.getMonthValue(); // Extraer el mes como entero
    
            // Asignar el valor al mes correspondiente (meses en 0-index, por eso restamos 1)
            data.set(monthResult - 1, totalExpense.intValue());
        }
    
        // Preparar la respuesta
        response.put("labels", months);
        response.put("data", data);
        response.put("label", "Gastos Anuales " + year);
    
        return response;
    }
    

}
    