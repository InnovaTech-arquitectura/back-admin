package com.innovatech.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Repository.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventService implements CrudService<EventEntity, Long> {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public EventEntity findById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Optional<EventEntity> findByIdOptional(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public EventEntity save(EventEntity entity) {
        return eventRepository.save(entity);
    }

    public List<EventEntity> findAll() {
        return eventRepository.findAll();
    }
}