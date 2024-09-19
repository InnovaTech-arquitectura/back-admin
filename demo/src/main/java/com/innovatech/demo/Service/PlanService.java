package com.innovatech.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.PlanEntity;
import com.innovatech.demo.Repository.PlanRepository;

@Service
public class PlanService implements CrudService<PlanEntity, Long>{

    @Autowired
    private PlanRepository planRepository;

    @Override
     public PlanEntity findById(Long id) {
        return  planRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteById(Long id) {
        planRepository.deleteById(id);
    }
    @Override
    public PlanEntity save(PlanEntity entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }


    
}
