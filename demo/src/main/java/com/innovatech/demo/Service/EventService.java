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

import java.sql.Date;

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
        // Guardar el evento en el repositorio
        EventEntity savedEvent = eventRepository.save(eventEntity);
        
        // Obtener la lista de emprendimientos desde el repositorio
        List<Entrepreneurship> entrepreneurships = entrepreneurshipRepository.findAll();
    
        // Crear asociaciones con emprendimientos si no existen ya en el registro
        List<Entrepreneurshipeventregistry> registrations = eventEntity.getEntrepreneurshipeventregistry();
        
        if (registrations == null || registrations.isEmpty()) {
            // Si no hay inscripciones en el evento, creamos nuevas asociaciones
            for (int i = 0; i < entrepreneurships.size(); i++) {
                Entrepreneurshipeventregistry registration = Entrepreneurshipeventregistry.builder()
                        .eventEntity(savedEvent)
                        .entrepreneurship(entrepreneurships.get(i)) // Asociamos con los emprendimientos
                        .date(Date.valueOf(eventEntity.getDate())) // Usamos la fecha del evento
                        .amountPaid(100.000 * (i + 1)) // Asignamos pagos simulados
                        .build();
                
                entrepreneurshipeventregistryRepository.save(registration);
            }
        } else {
            // Si ya hay inscripciones, actualizamos o guardamos las existentes
            for (Entrepreneurshipeventregistry registration : registrations) {
                if (registration.getId() != null) {
                    // Recuperamos la inscripción existente
                    Entrepreneurshipeventregistry existingRegistration = entrepreneurshipeventregistryRepository
                            .findById(registration.getId())
                            .orElse(null);
                    
                    if (existingRegistration != null) {
                        // Actualizamos los datos con los de la base de datos
                        registration.setId(existingRegistration.getId());
                        registration.setEventEntity(existingRegistration.getEventEntity());
                        registration.setEntrepreneurship(existingRegistration.getEntrepreneurship());
                    } else {
                        // Si no existe, la guardamos como nueva
                        entrepreneurshipeventregistryRepository.save(registration);
                    }
                } else {
                    // Guardamos inscripciones nuevas
                    entrepreneurshipeventregistryRepository.save(registration);
                }
            }
        }
    
        // Retornar el evento guardado
        return savedEvent;
    }
    

}