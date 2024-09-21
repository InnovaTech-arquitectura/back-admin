package com.innovatech.demo.init;

import java.util.Random;
import java.sql.Timestamp;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.innovatech.demo.Entity.Course;
import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Enum.Modality;
import com.innovatech.demo.Repository.RepositoryCourse;
import com.innovatech.demo.Repository.RepositoryEntrepreneurship;

@Profile({"default"})
@Component
public class DBInitializer implements CommandLineRunner{
    
    @Autowired
    private RepositoryCourse courseRepository;

    @Autowired
    private RepositoryEntrepreneurship entrepreneurshipRepository;

    public static final Modality PRESENCIAL = Modality.presencial;
    public static final Modality VIRTUAL = Modality.virtual;
    
    @Override
    public void run(String... args) throws Exception
    {
        System.out.println("Uploading entrepreneurships");
        insertEntrepreneurships();

        System.out.println("Uploading courses");
        insertCourses();

        System.out.println("all data uploaded");
    }

    private void insertEntrepreneurships()
    {
        Entrepreneurship newEntrepreneurship= new Entrepreneurship("Zara","", "ropa", "maria", "martinez");

        entrepreneurshipRepository.save(newEntrepreneurship);
    }

    private void insertCourses() {
    // Obtener la hora de las 3 PM
    LocalTime threePM = LocalTime.of(15, 0); // 3 PM

    // Crear la fecha de mañana a las 3 PM
    LocalDateTime tomorrowAtThreePM = LocalDateTime.of(LocalDate.now().plusDays(1), threePM);
    Timestamp timestampForTomorrow = Timestamp.valueOf(tomorrowAtThreePM); // Convertir LocalDateTime a Timestamp

    // Obtener la lista de emprendimientos
    List<Entrepreneurship> entrepreneurList = entrepreneurshipRepository.findAll();

    // Crear el nuevo curso con la fecha de mañana a las 3 PM
    Course newCourse = new Course(
        "https://teams.microsoft.com/l/meetup-join/19%3ameeting_NTAxZmMyMmItNzUwNS00Mjg0LWEzMTQtZTE0ZDNmZTRkNzQ2%40thread.v2/0?",
        "Capacitación para atraer más clientes",
        0f,
        timestampForTomorrow, // Usamos la fecha y hora de mañana a las 3 PM aquí
        "Cómo atraer más clientes",
        2,
        VIRTUAL, // Asegúrate de que el valor Modality esté correctamente importado y configurado
        entrepreneurList
    );

    // Guardar el nuevo curso en el repositorio
    courseRepository.save(newCourse);

    // Crear la fecha de ayer a las 3 PM
    LocalDateTime yesterdayAtThreePM = LocalDateTime.of(LocalDate.now().minusDays(1), threePM);
    Timestamp timestampForYesterday = Timestamp.valueOf(yesterdayAtThreePM); // Convertir LocalDateTime a Timestamp

    // Crear el segundo curso con la fecha de ayer a las 3 PM
    Course newCourse2 = new Course(
        "https://teams.microsoft.com/l/meetup-join/19%3ameeting_NTAxZmMyMmItNzUwNS00Mjg0LWEzMTQtZTE0ZDNmZTRkNzQ2%40thread.v2/0?",
        "Capacitación para mejorar ventas",
        0f,
        timestampForYesterday, // Usamos la fecha de ayer a las 3 PM aquí
        "Mejorar ventas fácil y rápido",
        2,
        PRESENCIAL, // Asegúrate de que el valor Modality esté correctamente importado y configurado
        entrepreneurList
    );

    // Guardar el segundo curso en el repositorio
    courseRepository.save(newCourse2);
}

}