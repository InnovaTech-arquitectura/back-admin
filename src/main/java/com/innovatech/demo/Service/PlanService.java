package com.innovatech.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Repository.FunctionalityRepository;
import com.innovatech.demo.Repository.PlanRepository;

@Service
public class PlanService implements CrudService<Plan, Long> {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private FunctionalityRepository functionalityRepository;

    @Override
    public Plan findById(Long id) {
        return planRepository.findById(id).orElse(null);
    }

    public Page<Plan> findAll(Pageable pageable) {
        return planRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        planRepository.deleteById(id);
    }

    public Plan findByName(String name) {
        return planRepository.findByName(name);
    }

    public List<Object[]> Subscriptionforplan() {

        return planRepository.Subscriptionforplan();
    }


    @Override
    public Plan save(Plan plan) {
        // Get the functionalities of the plan
        List<Functionality> functionalities = plan.getFunctionalities();
        for (Functionality functionality : functionalities) {
            // If the functionality already exists in the database, we recover it
            if (functionality.getId() != null) {
                Functionality existingFunctionality = functionalityRepository.findById(functionality.getId())
                        .orElse(null);
                if (existingFunctionality != null) {
                    functionality.setId(existingFunctionality.getId());
                    functionality.setName(existingFunctionality.getName());
                    functionality.setDescription(existingFunctionality.getDescription());
                } else {
                    functionalityRepository.save(functionality);
                }
            } else {
                // If the functionality does not exist in the database, we save it
                functionalityRepository.save(functionality);
            }
        }

        // Save the plan with the functionalities in the dbb
        return planRepository.save(plan);
    }

}
