package com.innovatech.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.OrderProduct;
import com.innovatech.demo.Repository.OrderProductRepository;

@Service
public class OrderProductService implements CrudService<OrderProduct, Long> {

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
    public OrderProduct findById(Long id) {
        return orderProductRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        orderProductRepository.deleteById(id);
    }

    @Override
    public OrderProduct save(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }
    
}
