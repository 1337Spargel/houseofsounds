package Factory;

import java.sql.ResultSet;

import java.sql.ResultSet;

public class LagerDaoImplement extends AbstractGenericDAO<Lager> {
    @Override
    protected Lager mapResultSetToEntity(ResultSet resultSet) throws Exception {
        Lager lager = new Lager();
        lager.setLager_ID(resultSet.getString("Lager_ID"));
        lager.setAnzahl_Produkte_Im_Lager(resultSet.getInt("Anzahl_Produkte_im_Lager"));
        lager.setStandort_ID((resultSet.getInt("Standort_ID")));

        lager.setProdukt_ID(resultSet.getInt("Produkt_ID"));
        return lager;
    }

    @Override
    protected String getTableName() {
        return "Lager";
    }


}

