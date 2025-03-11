package ro.mpp2024.domain;

public class Inscriere extends Entity<Integer>{

    private String participantName;
    private int participantAge;

    private Proba proba;
    public Proba getProba() {
        return proba;
    }
    public void setProba(Proba p) {
        this.proba = p;
    }
    public Inscriere(String participantName, int participantAge) {
        this.participantName = participantName;
        this.participantAge = participantAge;
    }

    public String getParticipantName() {
        return participantName;
    }
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
    public int getParticipantAge() {
        return participantAge;
    }
    public void setParticipantAge(int participantAge) {
        this.participantAge = participantAge;
    }

}
