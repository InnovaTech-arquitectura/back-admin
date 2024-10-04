package com.innovatech.demo.Service;

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

}