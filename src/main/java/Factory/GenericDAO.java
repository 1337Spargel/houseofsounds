package Factory;

import java.sql.ResultSet;
import java.util.List;

public interface GenericDAO<T> {
    List<T> getAll();

    T getById(int id);

    void save(T entity);

    void update(T entity);

    void delete(T entity);
}

