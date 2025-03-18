package ro.mpp2024.domain;

public class Participant extends Entity<Integer>{
    private String name;
    private int age;
    private int nrProbe;

    public Participant(String name, int age, int nrProbe) {
        this.name = name;
        this.age = age;
        this.nrProbe = nrProbe;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getNrProbe() {
        return nrProbe;
    }
    public void setNrProbe(int nrProbe) {
        this.nrProbe = nrProbe;
    }
}
