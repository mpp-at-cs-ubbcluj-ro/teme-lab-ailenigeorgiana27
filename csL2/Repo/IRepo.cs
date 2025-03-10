using lab2_mpp.domain;

namespace lab2_mpp.repo;

public interface IRepo<ID, E> where E: Entity<ID>
{
    void Add(E e);
    void Update(E e);
    void Remove(ID id);
    E GetById(ID id);
    IEnumerable<E> GetAll();
    
}