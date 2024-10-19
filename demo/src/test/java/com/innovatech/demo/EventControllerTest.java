package com.innovatech.demo;

import com.innovatech.demo.Controller.EventController;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Service.EventService;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEvents() {
        // Arrange
        EventEntity event1 = new EventEntity(1L, "Event1", 100, Timestamp.valueOf("2023-09-21 00:00:00"), Timestamp.valueOf("2023-09-21 00:00:00"), 50, 30, 10, "Modality1", null, null);
        EventEntity event2 = new EventEntity(2L, "Event2", 200, Timestamp.valueOf("2023-09-22 00:00:00"), Timestamp.valueOf("2023-09-23 00:00:00"),  75, 50, 10, "Modality2", null, null);
        
        List<EventEntity> events = Arrays.asList(event1, event2);
        Page<EventEntity> eventPage = new PageImpl<>(events, PageRequest.of(0, 10), events.size());
        
        when(eventService.findAll(PageRequest.of(0, 10))).thenReturn(eventPage);

        // Act
        ResponseEntity<?> response = eventController.getAllEvents(10, 0);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventPage, response.getBody());
    }


    @Test
    public void testGetEventById_Success() {
        // Arrange
        EventEntity event = new EventEntity(1L, "Event1", 100, Timestamp.valueOf("2023-09-21 00:00:00"), Timestamp.valueOf("2023-09-21 00:00:00"), 50, 30, 10, "Modality1", null, null);
                when(eventService.findById(1L)).thenReturn(event);

        // Act
        ResponseEntity<?> response = eventController.getEventById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    public void testGetEventById_NotFound() {
        // Arrange
        when(eventService.findById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = eventController.getEventById("1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Event not found", response.getBody());
    }


// Additional test for existing event
@Test
public void testAddEvent_AlreadyExists() {
    // Arrange
    EventEntity event = new EventEntity(1L, "Event1", 100, Timestamp.valueOf("2023-09-21 00:00:00"), 
                                         Timestamp.valueOf("2023-09-21 00:00:00"), 50, 30, 10, "Modality1", null, null);
    
    when(eventService.findByName("Event1")).thenReturn(event); // Event already exists

    // Act
    ResponseEntity<?> response = eventController.addEvent(event);

    // Assert
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
}


    @Test
    public void testAddEvent_Conflict() {
        // Arrange
        EventEntity existingEvent = new EventEntity(1L, "Event1", 100, Timestamp.valueOf("2023-09-21 00:00:00"), Timestamp.valueOf("2023-09-21 00:00:00"), 50, 30, 10, "Modality1", null, null);
        
        // Simula que el evento ya existe en el servicio
        when(eventService.findByName("Event1")).thenReturn(existingEvent);

        // Act
        ResponseEntity<?> response = eventController.addEvent(existingEvent);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        
        // Verifica que se llamó al servicio para buscar el evento
        verify(eventService).findByName("Event1");
}


    @Test
    public void testUpdateEvent_Success() {
        // Arrange
        EventEntity event = new EventEntity(1L, "Event1", 100, Timestamp.valueOf("2023-09-21 00:00:00"), Timestamp.valueOf("2023-09-21 00:00:00"), 50, 30, 10, "Modality1", null, null);
                when(eventService.findById(1L)).thenReturn(event);

        // Act
        ResponseEntity<?> response = eventController.updateEvent(event);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Event modified successfully", response.getBody());
    }

    @Test
    public void testUpdateEvent_NotFound() {
        // Arrange
        EventEntity event = new EventEntity(1L, "Event1", 100, Timestamp.valueOf("2023-09-21 00:00:00"), Timestamp.valueOf("2023-09-21 00:00:00"), 50, 30, 10, "Modality1", null, null);
                when(eventService.findById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = eventController.updateEvent(event);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Conflict: Event not found", response.getBody());
    }

    @Test
    public void testDeleteEvent_Success() {
        // Arrange
        EventEntity event = new EventEntity(1L, "Event1", 100, Timestamp.valueOf("2023-09-21 00:00:00"), Timestamp.valueOf("2023-09-21 00:00:00"), 50, 30, 10, "Modality1", null, null);
                when(eventService.findById(1L)).thenReturn(event);

        // Act
        ResponseEntity<?> response = eventController.deleteEvent(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Event deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteEvent_NotFound() {
        // Arrange
        when(eventService.findById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = eventController.deleteEvent(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Event not found", response.getBody());
    }
}
