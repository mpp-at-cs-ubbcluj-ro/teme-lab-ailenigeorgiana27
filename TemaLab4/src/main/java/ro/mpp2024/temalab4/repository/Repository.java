package ro.mpp2024.temalab4.repository;

import  ro.mpp2024.temalab4.model.Identifiable;

import java.util.Collection;

public interface Repository<Tid, T extends Identifiable<Tid>> {
    T  add(T elem);
    void delete(T elem);
    void update(T elem, Tid id);
    T findById(Tid id);
    Iterable<T> findAll();
    Collection<T> getAll();
}
