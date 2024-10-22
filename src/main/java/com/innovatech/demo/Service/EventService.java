package com.innovatech.demo.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Entrepreneurshipeventregistry;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Repository.EntrepreneurshipRepository;
import com.innovatech.demo.Repository.EntrepreneurshipeventregistryRepository;
import com.innovatech.demo.Repository.EventRepository;

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
        for (Object[] result : results) {
            // Obtenemos el año directamente como un entero
            int yearResult = ((Number) result[0]).intValue(); // El año devuelto por la consulta
            Double totalExpense = ((Number) result[1]).doubleValue(); // El total de gastos
    
            // No es necesario procesar el año como una fecha, simplemente trabaja con el mes y el año que tienes
            // Aquí puedes aplicar lógica para dividir el total de gastos por meses si fuera necesario.
            // Sin embargo, dado que no tienes información específica de los meses en tu consulta actual,
            // este paso no sería relevante.
            // Puedes agregar la lógica para manejar meses si la consulta devolviera datos por mes.
        }
    
        // Preparar la respuesta
        response.put("labels", months);
        response.put("data", data);
        response.put("label", "Gastos Anuales " + year);
    
        return response;
    }
    
    
    
    

}
    


