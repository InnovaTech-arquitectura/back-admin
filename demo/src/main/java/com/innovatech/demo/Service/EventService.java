package com.innovatech.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Repository.EventRepository;

@Service
public class EventService implements CrudService<EventEntity, Long>{
    

    @Autowired
    private EventRepository eventRepository;

    @Override
     public EventEntity findById(Long id) {
        return  eventRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }
    @Override
    public EventEntity save(EventEntity entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
