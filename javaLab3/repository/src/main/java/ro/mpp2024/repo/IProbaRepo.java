package ro.mpp2024.repo;


import ro.mpp2024.domain.Proba;



public interface IProbaRepo extends IRepo<Integer, Proba> {
        Iterable<Proba> getbyDistanceAndStil(String distance, String stil) throws EntityRepoException;
}
