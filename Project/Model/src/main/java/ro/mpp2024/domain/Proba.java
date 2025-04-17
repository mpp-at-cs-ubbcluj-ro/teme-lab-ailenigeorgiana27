package ro.mpp2024.domain;

import ro.mpp2024.domain.enums.Stil;

import java.io.Serializable;
import java.util.Objects;

public class Proba extends Entity<Integer>{
    private float distanta;
    private Stil stil;
    private int nrParticipants;

    public Proba(float distanta, Stil stil) {
        this.distanta = distanta;
        this.stil = stil;
    }

    public Proba(int id,float distanta, Stil stil) {
        this.setId(id);
        this.distanta = distanta;
        this.stil = stil;
    }
    public int getNrParticipants() {
        return nrParticipants;
    }
    public void setNrParticipants(int nrParticipants) {
        this.nrParticipants = nrParticipants;
    }

    public Proba() {

    }

    public float getDistanta() {
        return distanta;
    }

    public void setDistanta(float distanta) {
        this.distanta = distanta;
    }

    public Stil getStil() {
        return stil;
    }

    public void setStil(Stil stil) {
        this.stil = stil;
    }

    @Override
    public String toString() {
        return "Proba{" +
                "distanta=" + distanta +
                ", stil=" + stil +
                '}';
    }

}
