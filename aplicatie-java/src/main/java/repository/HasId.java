package repository;

import java.util.UUID;

public interface HasId<ID> {
    ID getId();
    void setId(ID id);
}