package ro.mpp2024.domain;

import java.io.Serializable;

public class Participant extends Entity<Integer> {
    private String name;
    private int age;
    private int nrProbe;


    public Participant(String name, int age) {
        this.name = name;
        this.age = age;

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
    @Override
    public String toString() {
        return "Participant{id=" + getId() + ", name=" + name + ", age=" + age + "}";
    }
}
