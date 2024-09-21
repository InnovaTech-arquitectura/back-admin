package com.innovatech.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Repository.PlanRepository;

@Service
public class PlanService implements CrudService<Plan, Long> {

    @Autowired
    private PlanRepository planRepository;

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

    @Override
    public Plan save(Plan entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
