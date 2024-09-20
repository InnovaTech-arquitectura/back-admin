package com.innovatech.demo;

import com.innovatech.demo.Controller.EventController;
import com.innovatech.demo.DTO.EventDTO;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // Agregar esto para que Mockito funcione
public class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    public void testGetAllEvents() {
        // Mocking the eventService response
        EventEntity event = new EventEntity();
        event.setName("Event Test");
        when(eventService.findAll()).thenReturn(Collections.singletonList(event));

        // Calling the method
        ResponseEntity<List<EventEntity>> response = eventController.getAllEvents();

        // Asserting the result
        assertEquals(1, response.getBody().size());
        assertEquals("Event Test", response.getBody().get(0).getName());
    }

    @Test
    public void testCreateEvent() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setName("New Event");

        EventEntity eventEntity = new EventEntity();
        eventEntity.setName("New Event");

        when(eventService.save(Mockito.any(EventEntity.class))).thenReturn(eventEntity);

        ResponseEntity<EventEntity> response = eventController.createEvent(eventDTO);

        assertEquals("New Event", response.getBody().getName());
    }
}