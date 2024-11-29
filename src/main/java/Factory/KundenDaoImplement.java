
package Factory;

import java.sql.ResultSet;

public class KundenDaoImplement extends AbstractGenericDAO<Kunden> {
    @Override
    protected Kunden mapResultSetToEntity(ResultSet resultSet) throws Exception {
        Kunden kunde = new Kunden();
        kunde.setKunden_ID(resultSet.getInt("kunden_id"));
        kunde.setVorname(resultSet.getString("vorname"));
        kunde.setNachname(resultSet.getString("nachname"));
        kunde.setStraße(resultSet.getString("straße"));
        kunde.setHausnummer(resultSet.getString("hausnummer"));
        kunde.setPLZ_ID(resultSet.getInt("PLZ_ID"));
        return kunde;
    }

    @Override
    protected String getTableName() {
        return "Kunden";
    }


}
