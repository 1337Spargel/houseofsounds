package Factory;

import java.util.List;

public interface GenericDAO<T> {
    List<T> getAll();

    T getById(int id);

    void save(T entity);

    Kunden update(T entity);

    void delete(T entity);
}

