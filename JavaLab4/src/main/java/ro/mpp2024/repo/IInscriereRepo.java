package ro.mpp2024.repo;

import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;

import java.util.List;

public interface IInscriereRepo extends IRepo<Integer, Inscriere> {
    List<Inscriere> getInscrierePentruParticipant(Participant participant) throws EntityRepoException;
    List<Inscriere> getInscrierePentruProba(Proba proba) throws EntityRepoException;
}
