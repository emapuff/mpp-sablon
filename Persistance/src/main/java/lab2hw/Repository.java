package lab2hw;

import lab2hw.Entity;

import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {

    List<E> findAll();

    E save(E entity);
}
