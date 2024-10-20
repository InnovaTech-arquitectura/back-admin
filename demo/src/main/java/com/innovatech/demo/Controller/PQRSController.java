package com.innovatech.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class PQRSController {

    public static class Question {
        private String texto;
        private String respuesta;

        public Question(String texto, String respuesta) {
            this.texto = texto;
            this.respuesta = respuesta;
        }

        public String getTexto() {
            return texto;
        }

        public String getRespuesta() {
            return respuesta;
        }
    }

    private List<Question> preguntas = new ArrayList<>();

    public PQRSController() {
        preguntas.add(new Question("Cómo puedo suscribirme a un plan?", "Suscribirse a un plan es fácil, solo necesitas..."));
        preguntas.add(new Question("Cómo puedo gestionar las finanzas de mi emprendimiento?", "Puedes gestionar tus finanzas utilizando la sección..."));
        preguntas.add(new Question("Puedo suscribirme a la cantidad de bazares que desee?", "Sí, puedes suscribirte a tantos bazares como desees."));
        preguntas.add(new Question("Para qué puedo utilizar la publicidad en Made-in?", "La publicidad en Made-in te permite..."));
        preguntas.add(new Question("Cómo puedo gestionar los permisos de mis empleados en la página?", "Gestionar los permisos de los empleados es sencillo..."));
        preguntas.add(new Question("Puedo inscribirme en la cantidad de capacitaciones que desee?", "Sí, puedes inscribirte en tantas capacitaciones como desees."));
    }

    @GetMapping
    public ResponseEntity<List<String>> obtenerPreguntas() {
        List<String> soloPreguntas = new ArrayList<>();
        for (Question pregunta : preguntas) {
            soloPreguntas.add(pregunta.getTexto());
        }
        return ResponseEntity.ok(soloPreguntas);
    }

    @GetMapping("/{index}/answer")
    public ResponseEntity<String> obtenerRespuesta(@PathVariable int index) {
        if (index >= 0 && index < preguntas.size()) {
            return ResponseEntity.ok(preguntas.get(index).getRespuesta());
        } else {
            return ResponseEntity.badRequest().body("Índice no válido");
        }
    }

    @DeleteMapping("/{index}")
    public ResponseEntity<String> eliminarPregunta(@PathVariable int index) {
        return ResponseEntity.ok("Pregunta eliminada");
    }

    @PutMapping("/{index}")
    public ResponseEntity<String> responderPregunta(@PathVariable int index) {
        return ResponseEntity.ok("Pregunta editada");
    }

    @PostMapping("")
    public ResponseEntity<String> agregarPregunta() {
        return ResponseEntity.ok("Pregunta agregada");
    }
}
