package Factory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



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
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + getTableName() + " WHERE " + getIdColumname() + " = ?")) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Die Methode mapResultSetToEntity aufrufen, um das ResultSet in die Entität zu konvertieren
                    return mapResultSetToEntity(resultSet);
                }
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
    public void update(T entity) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Field primaryKeyField = findPrimaryKeyField(entity.getClass());

            System.out.println("Erfolg: " + primaryKeyField);
            if (primaryKeyField == null) {
                throw new IllegalArgumentException("Kein Primärschlüsselfeld gefunden");
            }

            List<String> updateColumns = new ArrayList<>();
            List<Object> updateValues = new ArrayList<>();
            Object primaryKeyValue = null;

            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    Object value = field.get(entity);

                    if (value != null && !field.equals(primaryKeyField)) {
                        updateColumns.add(field.getName() + " = ?");
                        updateValues.add(value);
                    }

                    if (field.getName().equals(primaryKeyField.getName())) {
                        primaryKeyValue = value;
                    }


                } catch (IllegalAccessException e) {
                    System.err.println("Konnte nicht auf Feld zugreifen: " + field.getName());
                }
            }

            if (primaryKeyValue == null) {
                throw new IllegalArgumentException("Primärschlüsselwert muss für Update gesetzt sein");
            }

            String updateQuery = "UPDATE " + getTableName() +
                    " SET " + String.join(", ", updateColumns) +
                    " WHERE " + primaryKeyField.getName() + " = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                int parameterIndex = 1;
                for (Object value : updateValues) {
                    preparedStatement.setObject(parameterIndex++, value);
                }

                preparedStatement.setObject(parameterIndex, primaryKeyValue);

                int betroffeneZeilen = preparedStatement.executeUpdate();

                if (betroffeneZeilen == 0) {
                    throw new SQLException("Update fehlgeschlagen, keine Zeilen betroffen");
                }
            }

        } catch (SQLException e) {
            System.err.println("Datenbankfehler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(T entity) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // Primärschlüsselfeld finden
            Field primaryKeyField = findPrimaryKeyField(entity.getClass());
            if (primaryKeyField == null) {
                throw new IllegalArgumentException("Kein Primärschlüsselfeld für Löschvorgang gefunden");
            }

            // Wert des Primärschlüssels ermitteln
            primaryKeyField.setAccessible(true);
            Object primaryKeyValue = primaryKeyField.get(entity);
            if (primaryKeyValue == null) {
                throw new IllegalArgumentException("Primärschlüsselwert muss für Löschvorgang gesetzt sein");
            }

            // Hauptdatensatz löschen
            String deleteQuery = "DELETE FROM " + getTableName() +
                    " WHERE " + primaryKeyField.getName() + " = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setObject(1, primaryKeyValue);

                int betroffeneZeilen = preparedStatement.executeUpdate();

                if (betroffeneZeilen == 0) {
                    System.err.println("Löschvorgang fehlgeschlagen: Keine Zeilen betroffen");
                } else {
                    System.out.println("Entität erfolgreich gelöscht: " + betroffeneZeilen + " Zeilen");
                }
            }
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Fehler beim Zugriff auf das Primärschlüsselfeld", e);
        }
    }


    // Helper method to find primary key field
    private Field findPrimaryKeyField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().toUpperCase().endsWith("ID")) {
                return field;
            }
        }
        return null;
    }

    private T mapResultSetToEntity(ResultSet resultSet, Class<T> entityClass) throws SQLException {
        try {

            T entity = entityClass.getDeclaredConstructor().newInstance();


            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);


                String columnName = field.getName();


                if (resultSetMetaDataContainsColumn(resultSet, columnName)) {
                    Object value = resultSet.getObject(columnName);
                    field.set(entity, value);
                }
            }

            return entity;
        } catch (Exception e) {
            throw new SQLException("Fehler beim Mappen des ResultSets auf die Entität", e);
        }
    }


    private boolean resultSetMetaDataContainsColumn(ResultSet resultSet, String columnName) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }

    protected String getIdColumname() {
        return getTableName() + "_ID";
    }
}
