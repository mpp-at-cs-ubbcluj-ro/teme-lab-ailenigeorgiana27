package ro.mpp2024.domain;

public class Entity<ID> {
    ID id;

    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }
}
