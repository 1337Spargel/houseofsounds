package Factory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractGenericDAO<T> implements GenericDAO<T> {
    private static final String URL = "jdbc:mysql://localhost:3306/houseofsounds";
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

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + getTableName() + "WHERE ID = ?")) {

            while (resultSet.next()) {
                T entity = mapResultSetToEntity(resultSet);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(T entity) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Field[] fields = entity.getClass().getDeclaredFields();

            List<String> columnNames = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (Field field : fields) {
                field.setAccessible(true);

                try {
                    Object value = field.get(entity);
                    if (value != null) {
                        columnNames.add(field.getName());
                        values.add(value);
                    }

                } catch (IllegalAccessException e) {
                    System.err.println("Couldnt access field: " + field.getName());
                }
            }
            String columns = String.join(",", columnNames);
            String placeholders = columnNames.stream()
                    .map(col -> "?")
                    .collect(Collectors.joining(", "));

            String insertQuery = "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + placeholders + ")";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                for (int i = 0; i < values.size(); i++) {
                    preparedStatement.setObject(i + 1, values.get(i));
                }

                preparedStatement.executeUpdate();

            }

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName().toLowerCase();
    }

    @Override
    public Kunden update(T entity) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Primärschlüsselfeld finden
            Field primaryKeyField = findPrimaryKeyField(entity.getClass());

            System.out.println("Erfolg: " + primaryKeyField);
            if (primaryKeyField == null) {
                throw new IllegalArgumentException("Kein Primärschlüsselfeld gefunden");
            }

            // Listen für Update-Spalten vorbereiten
            List<String> updateColumns = new ArrayList<>();
            List<Object> updateValues = new ArrayList<>();
            Object primaryKeyValue = null;

            // Durch Felder iterieren
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    Object value = field.get(entity);

                    // Null-Werte und Primärschlüsselfeld überspringen
                    if (value != null && !field.equals(primaryKeyField)) {
                        updateColumns.add(field.getName() + " = ?");
                        updateValues.add(value);
                    }

                    // Primärschlüsselwert erfassen
                    if (field.getName().equals(primaryKeyField.getName())) {
                        primaryKeyValue = value;
                    }


                } catch (IllegalAccessException e) {
                    System.err.println("Konnte nicht auf Feld zugreifen: " + field.getName());
                }
            }

            // Primärschlüssel prüfen
            if (primaryKeyValue == null) {
                throw new IllegalArgumentException("Primärschlüsselwert muss für Update gesetzt sein");
            }

            // Update-Abfrage konstruieren
            String updateQuery = "UPDATE " + getTableName() +
                    " SET " + String.join(", ", updateColumns) +
                    " WHERE " + primaryKeyField.getName() + " = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                // Update-Werte setzen
                int parameterIndex = 1;
                for (Object value : updateValues) {
                    preparedStatement.setObject(parameterIndex++, value);
                }

                // Primärschlüsselwert am Ende setzen
                preparedStatement.setObject(parameterIndex, primaryKeyValue);

                // Update ausführen
                int betroffeneZeilen = preparedStatement.executeUpdate();

                if (betroffeneZeilen == 0) {
                    throw new SQLException("Update fehlgeschlagen, keine Zeilen betroffen");
                }
            }

        } catch (SQLException e) {
            System.err.println("Datenbankfehler: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    // Helper method to find primary key field
    private Field findPrimaryKeyField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase("kunden_ID")) { // Nach Feldnamen suchen
                return field;
            }
        }
        return null;
    }



    @Override
    public void delete(T entity) {
        // Implementiere die Methode, um eine Entität zu löschen
    }
}