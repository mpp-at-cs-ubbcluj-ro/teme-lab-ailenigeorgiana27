package ro.mpp2024.services;

import ro.mpp2024.domain.Proba;

public interface IObserver {


    void newInscriere() throws AppException;
}