package com.innovatech.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Repository.AdministrativeEmployeeRepository;

@Service
public class AdministrativeEmployeeService implements CrudService<AdministrativeEmployee, Long> {
    @Autowired
    private AdministrativeEmployeeRepository administrativeEmployeeRepository;

    @Override
    public AdministrativeEmployee save(AdministrativeEmployee administrativeEmployee) {
        return administrativeEmployeeRepository.save(administrativeEmployee);
    }

    @Override
    public AdministrativeEmployee findById(Long id) {
        return administrativeEmployeeRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        administrativeEmployeeRepository.deleteById(id);
    }

    public Page<AdministrativeEmployee> findAll(Pageable pageable) {
        return administrativeEmployeeRepository.findAll(pageable);
    }

    public boolean existsById(Long id) {
        return administrativeEmployeeRepository.existsById(id);
    }

    public List<AdministrativeEmployee> findByRoleId(Long roleId) {
        return administrativeEmployeeRepository.findByRoleId(roleId);
    }

}
