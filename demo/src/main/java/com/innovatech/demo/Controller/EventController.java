package com.innovatech.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.demo.Entity.EventEntity;
import com.innovatech.demo.Service.EventService;

@RestController
@RequestMapping("event")
@CrossOrigin(origins = "http://10.43.100.240:4200/")
public class EventController {

    @Autowired
    private EventService eventService;

    // http://localhost:8090/event/all?limit=n&page=m
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return ResponseEntity.ok(eventService.findAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
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

    // http://localhost:8090/event/add
    @PostMapping("/add")
    public ResponseEntity<?> addEvent(@RequestBody EventEntity event) {
        try {
            EventEntity eventname = eventService.findByName(event.getName());

            if (eventname != null) {
                // Handle event with the same name already existing (e.g., HttpStatus.CONFLICT)
                return new ResponseEntity<>("Event with the same name already exists", HttpStatus.CONFLICT);
            }

            // Save the new event
            EventEntity eventDB = eventService.save(event);

            if (eventDB == null) {
                // Handle error saving the event (more specific message?)
                return new ResponseEntity<>("Unable to save event", HttpStatus.INTERNAL_SERVER_ERROR); // Or a more
                                                                                                       // specific error
                                                                                                       // code
            }

            return new ResponseEntity<>(eventDB, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle unexpected exceptions (log the error)
            return new ResponseEntity<>("Unable to add event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // http://localhost:8090/event/update
    @PutMapping("/update")
    public ResponseEntity<?> updateEvent(@RequestBody EventEntity event) {
        try {
            EventEntity existingEvent = eventService.findById(event.getId());

            if (existingEvent == null) {
                return new ResponseEntity<>("Conflict: Event not found", HttpStatus.NOT_FOUND);
            }

            eventService.save(event);
            return new ResponseEntity<>("Event modified successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to modify Event", HttpStatus.BAD_REQUEST);
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
