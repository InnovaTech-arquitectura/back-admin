package com.innovatech.demo.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Repository.EntrepreneurshipRepository;

@Service
public class EntrepreneurshipService implements CrudService<Entrepreneurship, Long> {

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    public Entrepreneurship findByName(String name) {
        return entrepreneurshipRepository.findByName(name);
    }

    public List<Entrepreneurship> findAllEntrepreneurships() {
        return entrepreneurshipRepository.findAllEntrepreneurships();
    }

    public Page<Entrepreneurship> findAll(Pageable pageable) {
        return entrepreneurshipRepository.findAll(pageable);
    }

    // Dashboard
    public List<Map<String, Object>> findAllWithActivePlan() {
        List<Object[]> result = entrepreneurshipRepository.findAllWithActivePlan();

        // Prepara la respuesta en el formato deseado
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : result) {
            Entrepreneurship entrepreneurship = (Entrepreneurship) row[0];
            String planName = (String) row[1];  // Nombre del plan

            // Crea el JSON de emprendimiento con los campos
            Map<String, Object> item = new HashMap<>();
            item.put("id", entrepreneurship.getId());
            item.put("name", entrepreneurship.getName());
            item.put("logo", entrepreneurship.getLogo());
            item.put("description", entrepreneurship.getDescription());
            item.put("names", entrepreneurship.getNames());
            item.put("lastnames", entrepreneurship.getLastnames());
            item.put("plan_name", planName);  // Agrega el nombre del plan

            response.add(item);
        }

        return response;
    }


    // Dashboard
    public List<Object[]> IngresosTotalesEntrepreneurships() {
        return entrepreneurshipRepository.IngresosTotalesEntrepreneurships();
    }

    // Dashboard 2
    public Map<String, Object> getIncomeForSpecificMonth(int month, int year) {
        List<Object[]> results = entrepreneurshipRepository.getIncomeByMonthAndYear(month, year);

        // Inicializar el mapa de respuesta
        Map<String, Object> response = new HashMap<>();

        // Procesar el resultado
        if (!results.isEmpty()) {
            Object[] result = results.get(0); // Solo habrá un resultado para el mes
            Integer monthResult = (Integer) result[0];
            Double totalIncome = (Double) result[1];

            response.put("month", monthResult); // El mes
            response.put("year", year); // El año
            response.put("totalIncome", totalIncome.intValue()); // El ingreso total
        } else {
            // Si no hay ingresos, devolver 0
            response.put("month", month);
            response.put("year", year); // El año
            response.put("totalIncome", 0);
        }

        return response;
    }

    // Dashboard 3
    public Map<String, Object> getIncomeByYear(int year) {
        List<Object[]> results = entrepreneurshipRepository.getAnnualIncomeByMonth(year);

        // Inicializar el mapa con los meses
        Map<String, Object> response = new HashMap<>();
        String[] months = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                "Octubre", "Noviembre", "Diciembre" };
        List<Integer> data = new ArrayList<>(Collections.nCopies(12, 0)); // Inicializa una lista de 12 ceros

        // Procesar los resultados
        for (Object[] result : results) {
            Integer monthResult = (Integer) result[0]; // El mes
            Double totalIncome = (Double) result[1]; // El total de ingresos

            // Asignar el valor al mes correspondiente
            data.set(monthResult - 1, totalIncome.intValue());
        }

        // Preparar la respuesta
        response.put("labels", months);
        response.put("data", data);
        response.put("label", "Ingresos Anuales " + year);

        return response;
    }

    @Override
    public Entrepreneurship save(Entrepreneurship entity) {
        return entrepreneurshipRepository.save(entity);
    }

    @Override
    public Entrepreneurship findById(Long id) {
        return entrepreneurshipRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        entrepreneurshipRepository.deleteById(id);
    }

}
