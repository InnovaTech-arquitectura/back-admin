package com.innovatech.demo.Controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.demo.Entity.Course;
import com.innovatech.demo.Entity.DTO.CourseDTONoID;
import com.innovatech.demo.Service.ServiceCourse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/course")
@CrossOrigin(origins = "http://localhost:4200/")
public class ControllerCourse {
    
    @Autowired
    private ServiceCourse courseService;

    @GetMapping("/all/active")
    public ResponseEntity<?>  listActiveCourses() {

        List <Course> activeCourses=courseService.listActiveCourses();
        if(activeCourses!=null)
        {
            return ResponseEntity.ok(activeCourses);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There are no active courses");
        }
    } 

    @GetMapping("/all")
    public ResponseEntity<?>  listCourses() {  
        List <Course> coursesList=courseService.listCourses();
        if(coursesList!=null)
        {
            return ResponseEntity.ok(coursesList);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There are no courses");
        }
    } 

    @GetMapping("/{id}")
    public ResponseEntity<?> findCourse(@PathVariable Long id) {  
        try {
            Course foundCourse = courseService.findCourse(id);
            return ResponseEntity.ok(foundCourse);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course does not exist");
        }
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<?>  editCourse(@PathVariable Long id, @RequestBody CourseDTONoID editedCourseDto) {   
        try {
            Course editedCourse=new Course(id,
                                    editedCourseDto.getLink(), 
                                    editedCourseDto.getDescription(),
                                    editedCourseDto.getScore(), 
                                    editedCourseDto.getDate(),
                                    editedCourseDto.getTitle(),
                                    editedCourseDto.getPlaces(),
                                    editedCourseDto.getModality());
            editedCourse=courseService.editCourse(editedCourse);
            return ResponseEntity.ok(editedCourse);

        } catch (DataIntegrityViolationException e) {
            // Err
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("The course title is already in use");

        } catch (IllegalArgumentException e) {
            // Manejo de excepción para argumentos inválidos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body(e.getMessage());
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?>  createCourse(@RequestBody CourseDTONoID newCourseDto) {   
        try {
            Course newCourse=new Course(newCourseDto.getLink(), 
                                        newCourseDto.getDescription(),
                                    0f, 
                                    newCourseDto.getDate(),
                                    newCourseDto.getTitle(),
                                    newCourseDto.getPlaces(),
                                    newCourseDto.getModality());
            newCourse=courseService.addCourse(newCourse);
            return ResponseEntity.ok(newCourse);

        } catch (DataIntegrityViolationException e) {
            // Err
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("The course title is already in use");

        } catch (IllegalArgumentException e) {
            // Manejo de excepción para argumentos inválidos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body(e.getMessage());

        }
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  deleteCourse(@PathVariable Long id) {   
        try
        {
             courseService.deleteCourse(id);
             return ResponseEntity.ok("Course successfully deleted");
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course does not exist");
        }
       
    }
}
