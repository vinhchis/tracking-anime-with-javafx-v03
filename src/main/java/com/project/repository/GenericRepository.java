package com.project.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    T save(T entity); // create + update
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(T entity);
    void deleteById(ID id);
    long count();
    void saveAll(Iterable<T> entities);
    List<T> findBy(String attributeName, Object value);
}
