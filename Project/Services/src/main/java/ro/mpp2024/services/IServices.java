package ro.mpp2024.services;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;

import java.util.List;

public interface IServices {
    void login(PersoanaOficiu user, IObserver client) throws AppException;
    void logout(PersoanaOficiu user, IObserver client) throws AppException;

    Proba[] getProbe() throws AppException;
    Participant[] getParticipantiDupaProba(Proba p) throws AppException, AppException;

    Proba[] getProbeDupaParticipanti(Participant p) throws AppException;

    void Inscrie(Participant participant, Proba[] probe)throws AppException;
    List<Integer> getNrParticipanti() throws AppException;
}
