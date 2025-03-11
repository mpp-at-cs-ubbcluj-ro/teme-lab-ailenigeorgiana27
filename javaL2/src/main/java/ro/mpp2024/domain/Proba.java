package ro.mpp2024.domain;

public class Proba extends Entity<Integer>{
    private String distance;
    private String stil;
    private int nrParticipants;

    public Proba(String distance, String stil, int nrParticipants) {
        this.distance = distance;
        this.stil = stil;
        this.nrParticipants = nrParticipants;
    }
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getStil() {
        return stil;
    }
    public void setStil(String stil) {
        this.stil = stil;
    }
    public int getNrParticipants() {
        return nrParticipants;
    }
    public void setNrParticipants(int nrParticipants) {
        this.nrParticipants = nrParticipants;
    }
}
