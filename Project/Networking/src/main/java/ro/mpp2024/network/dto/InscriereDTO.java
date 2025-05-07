package ro.mpp2024.network.dto;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;

import java.io.Serializable;

public class InscriereDTO implements Serializable {
    private Participant p;
    private Proba[] probas;

    public InscriereDTO(Participant p, Proba[] probas) {
        this.p = p;
        this.probas = probas;
    }

    public Participant getP() {
        return p;
    }

    public void setP(Participant p) {
        this.p = p;
    }

    public Proba[] getProbas() {
        return probas;
    }

    public void setProbas(Proba[] probas) {
        this.probas = probas;
    }
}
