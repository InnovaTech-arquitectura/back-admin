package com.innovatech.demo.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Entrepreneurshipeventregistry;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Repository.EntrepreneurshipeventregistryRepository;
import com.innovatech.demo.Repository.EventRepository;

@Service
public class EventService implements CrudService<EventEntity, Long> {

    @Autowired
    private EventRepository eventRepository;
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
        // Guardar el evento primero
        EventEntity savedEvent = eventRepository.save(eventEntity);
        
        // Asegurarse de que la lista de inscripciones no sea nula
        List<Entrepreneurship> registrations = eventEntity.getEntrepreneurships();
        if (registrations == null) {
            registrations = new ArrayList<>(); // Usa una lista temporal si es nula
        }

        for (Entrepreneurship registration : registrations) {
            // Solo registrar el emprendimiento si no está ya asociado
            Entrepreneurshipeventregistry existingRegistry = entrepreneurshipeventregistryRepository
                    .findByEventAndEntrepreneurship(savedEvent, registration);

            if (existingRegistry == null) {
                // Crear una nueva inscripción si no existe
                Entrepreneurshipeventregistry newRegistry = new Entrepreneurshipeventregistry(savedEvent, registration);
                entrepreneurshipeventregistryRepository.save(newRegistry);
            }
            // Aquí puedes optar por manejar actualizaciones si es necesario
        }

        return savedEvent; // Retornar el evento guardado
    }
    

    /* TODO: Fix this
    public Map<String, Object> getExpensesForSpecificMonth(int month, int year) {
        List<Object[]> results = eventRepository.getExpensesByMonthAndYear(month, year);

        // Inicializar el mapa de respuesta
        Map<String, Object> response = new HashMap<>();

        // Procesar el resultado
        if (!results.isEmpty()) {
            Object[] result = results.get(0); // Solo habrá un resultado para el mes
            Integer monthResult = (Integer) result[0];
            Double totalExpense = (Double) result[1];

            response.put("label", "Gastos del Mes");
            response.put("month", monthResult); // El mes
            response.put("totalExpense", totalExpense.intValue()); // El gasto total
        } else {
            // Si no hay gastos, devolver 0
            response.put("month", month);
            response.put("totalExpense", 0);
        }

        return response;
    }
        */

    
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