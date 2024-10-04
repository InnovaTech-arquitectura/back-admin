package com.innovatech.demo.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Entrepreneurship;

import com.innovatech.demo.Repository.EntrepreneurshipRepository;


@Service
public class EntrepreneurshipService implements CrudService<Entrepreneurship, Long>{

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    public Entrepreneurship findByName(String name) {
        return entrepreneurshipRepository.findByName(name);
    }

    public List<Entrepreneurship> findAll() {
        return entrepreneurshipRepository.findAll();
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
