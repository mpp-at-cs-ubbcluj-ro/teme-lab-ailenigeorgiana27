package ro.mpp2024.domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable {
    ID id;

    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }
}
