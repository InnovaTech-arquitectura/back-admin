package com.innovatech.demo.init;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

import com.innovatech.demo.Entity.Course;
import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Enum.Modality;
import com.innovatech.demo.Repository.RepositoryCourse;
import com.innovatech.demo.Repository.RepositoryEntrepreneurship;

@Profile({"default"})
@Component
public class DBInitializer implements CommandLineRunner {
    
    @Autowired
    private RepositoryCourse courseRepository;

    @Autowired
    private RepositoryEntrepreneurship entrepreneurshipRepository;

    public static final Modality PRESENCIAL = Modality.presencial;
    public static final Modality VIRTUAL = Modality.virtual;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Uploading entrepreneurships");
        insertEntrepreneurships();

        System.out.println("Uploading courses");
        insertCourses();

        System.out.println("All data uploaded");
    }

    private void insertEntrepreneurships() {
        Entrepreneurship zara = new Entrepreneurship("Zara", "", "ropa", "maria", "martinez");
        Entrepreneurship nike = new Entrepreneurship("Nike", "", "deporte", "juan", "perez");
    
        entrepreneurshipRepository.save(zara);
        entrepreneurshipRepository.save(nike);
    }
    
    private void insertCourses() {
        // Obtener la hora de las 3 PM
        LocalTime threePM = LocalTime.of(15, 0);
    
        // Crear la fecha de mañana a las 3 PM
        LocalDateTime tomorrowAtThreePM = LocalDateTime.of(LocalDate.now().plusDays(1), threePM);
        Timestamp timestampForTomorrow = Timestamp.valueOf(tomorrowAtThreePM);
    
        // Obtener la lista de emprendimientos
        List<Entrepreneurship> entrepreneurList = entrepreneurshipRepository.findAll();
    
        // Crear el nuevo curso
        Course newCourse = new Course(
            "https://teams.microsoft.com/l/meetup-join/19%3ameeting_NTAxZmMyMmItNzUwNS00Mjg0LWEzMTQtZTE0ZDNmZTRkNzQ2%40thread.v2/0?",
            "Capacitación para atraer más clientes",
            0f,
            timestampForTomorrow,
            "Cómo atraer más clientes",
            2,
            VIRTUAL
        );
    
        // Añadir los emprendimientos al curso
        for (Entrepreneurship entrepreneurship : entrepreneurList) {
            newCourse.addEntrepreneurship(entrepreneurship);
        }
    
        // Guardar el nuevo curso en el repositorio
        courseRepository.save(newCourse);

    
        // Crear la fecha de mañana a las 3 PM
        LocalDateTime yesterdayAtThreePM = LocalDateTime.of(LocalDate.now().minusDays(1), threePM);
        Timestamp timestampForYesterday = Timestamp.valueOf(yesterdayAtThreePM);

        Course newCourse2 = new Course(
            "https://teams.microsoft.com/l/meetup-join/19%3ameeting_NTAxZmMyMmItNzUwNS00Mjg0LWEzMTQtZTE0ZDNmZTRkNzQ2%40thread.v2/0?",
            "Capacitación para ser buen jefe",
            0f,
            timestampForYesterday,
            "Como ser buen jefe",
            3,
            VIRTUAL
        );
    
        // Añadir los emprendimientos al curso
        for (Entrepreneurship entrepreneurship : entrepreneurList) {
            newCourse2.addEntrepreneurship(entrepreneurship);
        }
    
        // Guardar el nuevo curso en el repositorio
        courseRepository.save(newCourse2);
    }
}
