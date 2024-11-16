package com.innovatech.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Client;
import com.innovatech.demo.Repository.ClientRepository;

@Service
public class ClientService implements CrudService<Client, Long> {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }
    
}
