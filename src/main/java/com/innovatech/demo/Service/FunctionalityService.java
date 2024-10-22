package com.innovatech.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Repository.FunctionalityRepository;

@Service
public class FunctionalityService implements CrudService<Functionality, Long> {

    @Autowired
    private FunctionalityRepository functionalityRepository;

    public Functionality findByName(String name) {
        return functionalityRepository.findByName(name);
    }

    public List<Functionality> findAll() {
        return functionalityRepository.findAll();
    }

    @Override
    public Functionality save(Functionality entity) {
        return functionalityRepository.save(entity);
    }

    @Override
    public Functionality findById(Long id) {
        return functionalityRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        functionalityRepository.deleteById(id);
    }

}
