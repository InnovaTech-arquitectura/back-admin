package com.innovatech.demo.Service;

import java.util.*;

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
        // Si es un evento nuevo, asegúrate de que no tenga ID
        if (eventEntity.getId() != null) {
            throw new RuntimeException("Event ID should be null for a new event.");
        }
    
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
    


     public EventEntity updateEvent(EventEntity event) {
        // Verifica si el evento existe
        Optional<EventEntity> existingEvent = eventRepository.findById(event.getId());
        if (!existingEvent.isPresent()) {
            throw new RuntimeException("Evento no encontrado con ID: " + event.getId());
        }

        // Actualiza y guarda el evento
        return eventRepository.save(event);
    }

}