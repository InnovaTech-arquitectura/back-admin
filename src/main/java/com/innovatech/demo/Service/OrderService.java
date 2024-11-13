package com.innovatech.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Order;
import com.innovatech.demo.Repository.OrderRepository;

@Service
public class OrderService implements CrudService<Order, Long> {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
    
}
