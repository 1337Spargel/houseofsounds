package Factory;

import java.sql.ResultSet;

public class ProdukteDaoImplement extends AbstractGenericDAO<Produkte> {
    @Override
    protected Produkte mapResultSetToEntity(ResultSet resultSet) throws Exception {
        Produkte produkt = new Produkte();
        produkt.setProdukte_ID(resultSet.getInt("Produkte_ID"));
        produkt.setPreis(resultSet.getDouble("Preis"));
        produkt.setRabatt_ID(resultSet.getInt("Rabatt_ID"));
        return produkt;
    }

    @Override
    protected String getTableName() {
        return "Produkte";
    }
}