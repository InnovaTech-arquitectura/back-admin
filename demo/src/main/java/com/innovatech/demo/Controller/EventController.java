package com.innovatech.demo.Controller;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Entrepreneurshipeventregistry;
import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Repository.EntrepreneurshipRepository;
import com.innovatech.demo.Repository.EntrepreneurshipeventregistryRepository;
import com.innovatech.demo.Service.EventService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private EntrepreneurshipeventregistryRepository entrepreneurshipeventregistryRepository;

    // http://localhost:8090/event/all
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return ResponseEntity.ok(eventService.findAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .contentType(MediaType.TEXT_PLAIN)
                             .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // http://localhost:8090/event/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable String id) {
        try {
            Long eventId = Long.parseLong(id);
            EventEntity event = eventService.findById(eventId);

            if (event == null) {
                return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(event, HttpStatus.OK);

        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEvent(@RequestBody EventEntity event) {
        try {
            // Verificar si el evento ya existe
            EventEntity existingEvent = eventService.findByName(event.getName());
            if (existingEvent != null) {
                return new ResponseEntity<>("Event with the same name already exists", HttpStatus.CONFLICT);
            }
    
            // Guardar el evento primero
            EventEntity savedEvent = eventService.save(event);
            
            // Asegúrate de que se guardó correctamente
            if (savedEvent == null) {
                return new ResponseEntity<>("Unable to save event", HttpStatus.INTERNAL_SERVER_ERROR);
            }
    
            // Ahora puedes agregar los registros de Entrepreneurshipeventregistry
            for (Entrepreneurshipeventregistry registry : savedEvent.getEntrepreneurshipeventregistry()) {
                // Establecer el evento actual para cada registro
                registry.setEventEntity(savedEvent);
            }
    
            // Guardar todos los registros de Entrepreneurshipeventregistry
            entrepreneurshipeventregistryRepository.saveAll(savedEvent.getEntrepreneurshipeventregistry());
    
            return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            return new ResponseEntity<>("Database error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // http://localhost:8090/event/update@PutMapping("/update") 
    @PutMapping("/update")
    public ResponseEntity<?> updateEvent(@RequestBody EventEntity event) {
        try {
            EventEntity existingEvent = eventService.findById(event.getId());

            // Si el evento no existe, retorna 404 Not Found
            if (existingEvent == null) {
                return new ResponseEntity<>("Conflict: Event not found", HttpStatus.NOT_FOUND);
            }

            // Actualiza los campos que deseas modificar
            existingEvent.setName(event.getName());
            existingEvent.setDate(event.getDate());
            // Agrega aquí otros campos que necesites actualizar

            // Guardar las inscripciones del evento
            existingEvent.setEntrepreneurshipeventregistry(event.getEntrepreneurshipeventregistry());

            eventService.save(existingEvent); // Guarda el evento actualizado

            // Retorna 200 OK si la actualización fue exitosa
            return new ResponseEntity<>("Event modified successfully", HttpStatus.OK);

        } catch (Exception e) {
            // Maneja cualquier otra excepción y retorna 400 Bad Request
            return new ResponseEntity<>("Unable to modify Event: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // http://localhost:8090/event/delete/{idEvent}
    @DeleteMapping("/delete/{idEvent}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long idEvent) {
        try {
            EventEntity existingEvent = eventService.findById(idEvent);

            if (existingEvent == null) {
                return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
            }

            eventService.deleteById(idEvent);
            return new ResponseEntity<>("Event deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to delete the event. Please check the request", HttpStatus.BAD_REQUEST);
        }
    }
}
