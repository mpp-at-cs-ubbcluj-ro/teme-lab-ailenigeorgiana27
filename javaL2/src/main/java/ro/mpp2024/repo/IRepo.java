package ro.mpp2024.repo;
import ro.mpp2024.domain.Entity;
import ro.mpp2024.repo.exceptions.EntityRepoException;

public interface IRepo<ID, E extends Entity<ID>> {
    E add (E e) throws EntityRepoException;
    E update (E e) throws EntityRepoException;
    E remove (E e) throws EntityRepoException;
    E getById (ID id) throws EntityRepoException;
    Iterable<E> getAll () throws EntityRepoException;
}
