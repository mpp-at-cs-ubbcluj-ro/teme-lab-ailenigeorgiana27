package ro.mpp2024.domain;

import java.io.Serializable;

public class Inscriere extends Entity<Integer>  {

    private Participant participant;

    private Proba proba;
    public Proba getProba() {
        return proba;
    }
    public void setProba(Proba p) {
        this.proba = p;
    }
    public Inscriere(Participant participant, Proba proba) {
        this.participant = participant;
        this.proba = proba;
    }

   public Participant getParticipant() {
        return participant;
   }
   public void setParticipant(Participant p) {
        this.participant = p;
   }

}
