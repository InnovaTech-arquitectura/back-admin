package com.innovatech.demo.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Entrepreneurshipeventregistry;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Repository.EntrepreneurshipeventregistryRepository;
import com.innovatech.demo.Repository.EventRepository;

@Service
public class EventService implements CrudService<EventEntity, Long> {

    @Autowired
    private EventRepository eventRepository;
    @Autowired

    private EntrepreneurshipeventregistryRepository EntrepreneurshipeventregistryRepository;

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
        // Asegurarse de que la lista de inscripciones no sea nula
        List<Entrepreneurshipeventregistry> registrations = eventEntity.getEntrepreneurshipeventregistry();
        if (registrations == null) {
            registrations = new ArrayList<>(); // Usa una lista temporal si es nula
        }

        for (Entrepreneurshipeventregistry registration : registrations) {
            registration.setEventEntity(eventEntity); // Establecer la relación bidireccional
            if (registration.getId() != null) {
                // Si ya existe, actualizarla
                Entrepreneurshipeventregistry existingRegistration = EntrepreneurshipeventregistryRepository
                        .findById(registration.getId())
                        .orElse(null);
                if (existingRegistration != null) {
                    registration.setDate(existingRegistration.getDate());
                    registration.setAmountPaid(existingRegistration.getAmountPaid());
                }
            }
        }

        // Guardar el evento y sus inscripciones
        return eventRepository.save(eventEntity);
    }
}