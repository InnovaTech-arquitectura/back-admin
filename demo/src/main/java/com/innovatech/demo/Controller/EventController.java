package com.innovatech.demo.Controller;

import com.innovatech.demo.DTO.EventDTO;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Get all events
    @GetMapping
    public ResponseEntity<List<EventEntity>> getAllEvents() {
        List<EventEntity> events = eventService.findAll();
        return ResponseEntity.ok(events);
    }

    // Get event by id
    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> getEventById(@PathVariable Long id) {
        return eventService.findByIdOptional(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new event
    @PostMapping
    public ResponseEntity<EventEntity> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        EventEntity eventEntity = convertDtoToEntity(eventDTO);
        EventEntity createdEvent = eventService.save(eventEntity);
        return ResponseEntity.ok(createdEvent);
    }

    // Update event by id
    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        return eventService.findByIdOptional(id)
                .map(existingEvent -> {
                    EventEntity updatedEvent = updateEntityFromDto(existingEvent, eventDTO);
                    eventService.save(updatedEvent);
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete event by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Utility method to convert DTO to Entity
    private EventEntity convertDtoToEntity(EventDTO eventDTO) {
        return EventEntity.builder()
                .name(eventDTO.getName())
                .Total_Cost(eventDTO.getTotal_Cost())
                .date(eventDTO.getDate())
                .Earnings(eventDTO.getEarnings())
                .CostoLocal(eventDTO.getCostoLocal())
                .place(eventDTO.getPlace())
                .modality(eventDTO.getModality())
                .Quota(eventDTO.getQuota())
                .build();
    }

    // Utility method to update existing entity from DTO
    private EventEntity updateEntityFromDto(EventEntity existingEvent, EventDTO eventDTO) {
        existingEvent.setName(eventDTO.getName());
        existingEvent.setTotal_Cost(eventDTO.getTotal_Cost());
        existingEvent.setDate(eventDTO.getDate());
        existingEvent.setEarnings(eventDTO.getEarnings());
        existingEvent.setCostoLocal(eventDTO.getCostoLocal());
        existingEvent.setPlace(eventDTO.getPlace());
        existingEvent.setModality(eventDTO.getModality());
        existingEvent.setQuota(eventDTO.getQuota());
        return existingEvent;
    }
}