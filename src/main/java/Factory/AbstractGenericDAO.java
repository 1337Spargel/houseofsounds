package Factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGenericDAO<T> implements GenericDAO<T> {
    private static final String URL = "jdbc:mysql://localhost:3306/houseofsound";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws Exception;
    protected abstract String getTableName();

    @Override
    public List<T> getAll() {
        List<T> entities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + getTableName())) {

            while (resultSet.next()) {
                T entity = mapResultSetToEntity(resultSet);
                entities.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }


    @Override
    public T getById(int id) {
        // Implementiere die Methode, um eine Entität nach ID zu holen
        return null;
    }

    @Override
    public void save(T entity) {
        // Implementiere die Methode, um eine Entität zu speichern
    }

    @Override
    public void update(T entity) {
        // Implementiere die Methode, um eine Entität zu aktualisieren
    }

    @Override
    public void delete(T entity) {
        // Implementiere die Methode, um eine Entität zu löschen
    }
}