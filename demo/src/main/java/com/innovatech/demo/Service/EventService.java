package com.innovatech.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Entrepreneurshipeventregistry;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Repository.EntrepreneurshipeventregistryRepository;
import com.innovatech.demo.Repository.EventRepository;

import java.util.List;


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

        
        // Obtener las inscripciones de emprendedores si existen
        List<Entrepreneurshipeventregistry> registrations = eventEntity.getEntrepreneurshipeventregistry();

        for (Entrepreneurshipeventregistry registration : registrations) {
            if (registration.getId() != null) {
                Entrepreneurshipeventregistry existingRegistration = EntrepreneurshipeventregistryRepository.findById(registration.getId())
                        .orElse(null);
                if (existingRegistration != null) {
                    // Actualizar la inscripción existente
                    registration.setId(existingRegistration.getId());
                    registration.setDate(existingRegistration.getDate()); 
                    registration.setAmountPaid(existingRegistration.getAmountPaid());
                } else {
                    // Si la inscripción no existe, se guarda una nueva
                    EntrepreneurshipeventregistryRepository.save(registration);
                }
            } else {
                // Si la inscripción no tiene ID, se guarda como nueva
                EntrepreneurshipeventregistryRepository.save(registration);
            }
        }

        // Guardar el evento con las inscripciones
        return eventRepository.save(eventEntity);
    }
}