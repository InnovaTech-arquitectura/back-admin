package com.innovatech.demo.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Course;
import com.innovatech.demo.Repository.RepositoryCourse;

@Service
public class ServiceCourse {
    
    @Autowired
    private RepositoryCourse courseRepository;

    public List<Course> listCourses() {
        return courseRepository.findAll();
    }

    public List<Course> listActiveCourses() {
        return courseRepository.findAllActive(LocalDateTime.now());
    }

    public Course findCourse(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    public Course addCourse(Course newCourse) {
        // Obtener la fecha y hora actuales como Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());
    
        // Evaluar si los lugares son mayores que 0 y si la fecha del curso es posterior a la fecha actual
        if (newCourse.getPlaces() > 0 && newCourse.getDate().after(now)) {
            // Lógica para agregar el curso
            return courseRepository.save(newCourse); // O cualquier otra lógica que necesites
        } else {
            throw new IllegalArgumentException("Places must be greater than 0 and date muyt be greater than todays date");
        }
    }

    public Course editCourse(Course editedCourse) {
        // Obtener la fecha y hora actuales como Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());
    
        // Evaluar si los lugares son mayores que 0 y si la fecha del curso es posterior a la fecha actual
        if (editedCourse.getPlaces() > 0 
            && editedCourse.getDate().after(now)) {
            
            if(!(
                editedCourse.getLink() == null || 
                editedCourse.getDescription() == null || 
                editedCourse.getDate() == null || 
                editedCourse.getTitle() == null || 
                editedCourse.getId() == null || 
                editedCourse.getScore() == null ||
                editedCourse.getModality() == null ||
                editedCourse.getPlaces() == null))
            {
                //evaluar si las nuevas plazas abarcan los ya inscritos
                Course courseFull=courseRepository.findById(editedCourse.getId()).orElseThrow();
                if(courseFull.getEntrepreneurships().size()<=editedCourse.getPlaces())
                {
                    // Lógica para agregar el curso
                    return courseRepository.save(editedCourse); // O cualquier otra lógica que necesites
                }
                else
                {
                    throw new IllegalArgumentException("Edited places must be equal o greater compared to the current subscribers");
                }
                
            }
            else
            {
                throw new IllegalArgumentException("All fields must be non-null and valid");
            }
        } else {
            throw new IllegalArgumentException("Places must be greater than 0 and date muyt be greater than todays date");
        }
    }

    public void deleteCourse(Long id) {
        Course deleteCourse=courseRepository.findById(id).orElseThrow();
        courseRepository.delete(deleteCourse);;
    }
}
