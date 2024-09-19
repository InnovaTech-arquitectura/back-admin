package com.innovatech.demo.Service;

import java.util.List;

public interface CrudService<T, ID> {
    T save(T entity);
    T findById(ID id);
    void deleteById(ID id);
}
