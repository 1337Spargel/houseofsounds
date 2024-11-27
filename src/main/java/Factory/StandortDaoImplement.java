package Factory;

import java.sql.ResultSet;

public class StandortDaoImplement extends AbstractGenericDAO<Standort> {
    @Override
    protected Standort mapResultSetToEntity(ResultSet resultSet) throws Exception {
        Standort standort = new Standort();
        standort.setStandort_ID(resultSet.getInt("Standort_ID"));
        standort.setStandortname(resultSet.getString("Standortname"));
        standort.setStraße(resultSet.getString("Straße"));
        standort.setHausnummer(resultSet.getString("Hausnummer"));
        standort.setPLZ_ID(resultSet.getInt("PLZ_ID"));
        return standort;
    }

    @Override
    protected String getTableName() {
        return "Standort";
    }
}
