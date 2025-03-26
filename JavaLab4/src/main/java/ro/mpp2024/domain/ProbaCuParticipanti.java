package ro.mpp2024.domain;

import ro.mpp2024.domain.enums.Stil;

public class ProbaCuParticipanti extends Entity<Integer> {
     Proba proba;
    private int nr;

    public ProbaCuParticipanti(Proba proba, int nr) {
        this.proba = proba;
        this.nr = nr;
    }

   public Proba getProba() {

        return proba;
   }
   public int getNr() {
        return nr;
   }
    @Override
    public String toString() {
        return proba.getDistance() + " " + proba.getStil() + " (" + nr + " participanți)";
    }
}
