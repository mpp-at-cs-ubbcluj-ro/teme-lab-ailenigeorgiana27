package ro.mpp2024.domain;

import java.util.Objects;

public class Proba extends Entity<Integer>{
    private String distance;
    private String stil;
    private Integer nrParticipants;

    public Proba(String distance, String stil) {
        this.distance = distance;
        this.stil = stil;

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
    public Integer getNrParticipants() {
        return nrParticipants;
    }
    public void setNrParticipants(Integer nrParticipants) {
        this.nrParticipants = nrParticipants;
    }
    @Override
    public String toString() {
        return String.format("%s %s (%d participanți)",
                distance, stil, nrParticipants != null ? nrParticipants : 0);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proba proba = (Proba) o;
        return Objects.equals(id, proba.id); // Compară după un identificator unic, de exemplu, ID-ul probei
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Asigură-te că hashCode este și el suprascris
    }


}
