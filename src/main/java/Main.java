import Factory.*;
import java.sql.SQLRecoverableException;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static final CopyOnWriteArrayList<Kunden> kundenList = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Produkte> produkteList = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Standort> standortList = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Lager> lagerList = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Rabatt> rabattList = new CopyOnWriteArrayList<>();

    public static final GenericDAO<Kunden> kundenDAO = DAOFactory.getDAO(Kunden.class);
    public static final GenericDAO<Produkte> produkteDAO = DAOFactory.getDAO(Produkte.class);
    public static final GenericDAO<Standort> standortDAO = DAOFactory.getDAO(Standort.class);
    public static final GenericDAO<Rabatt> rabattDAO = DAOFactory.getDAO(Rabatt.class);
    public static final GenericDAO<Lager> lagerDAO = DAOFactory.getDAO(Lager.class);

    private static volatile boolean isRunning = false;
    private static Thread dataLoaderThread;

    public static void startDataLoader() {
        if (isRunning) return;

        isRunning = true;
        dataLoaderThread = new Thread(() -> {
            while (isRunning) {
                try {

                    kundenList.clear();
                    produkteList.clear();
                    standortList.clear();
                    rabattList.clear();
                    lagerList.clear();

                    kundenList.addAll(kundenDAO.getAll());

                    produkteList.addAll(produkteDAO.getAll());

                    standortList.addAll(standortDAO.getAll());
                    rabattList.addAll(rabattDAO.getAll());
                    lagerList.addAll(lagerDAO.getAll());

                    System.out.println("Daten aktualisiert: " +
                            kundenList.size() + " Kunden, " +
                            produkteList.size() + " Produkte, " +
                            standortList.size() + " Standort, " +
                            rabattList.size() + " Rabatte, " +
                            lagerList.size() + " Lager, ");

                    Thread.sleep(5000); // Wartezeit
                } catch (Exception e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            }
        });

        dataLoaderThread.setDaemon(true);
        dataLoaderThread.start();
    }

    public static void stopDataLoader() {
        isRunning = false;
        if (dataLoaderThread != null) {
            dataLoaderThread.interrupt();
        }
    }

    public static void clearKundenList() {
        kundenList.clear();
    }

    public static void clearProdukteList() {
        produkteList.clear();
    }
    public static void clearstandortList() {
        standortList.clear();
    }
    public static void clearrabattList() {
        rabattList.clear();
    }
    public static void clearlagerlist() {
        lagerList.clear();
    }

    public static List<Kunden> getKundenList() {
        return kundenList;
    }

    public static List<Produkte> getProdukteList() {
        return produkteList;
    }

    public static List<Standort> getStandortList() {
        return standortList;
    }
    public static List<Rabatt> getRabattList() {
        return rabattList;
    }
    public static List<Lager> getLagerList() {
        return lagerList;
    }

    public static void main(String[] args) {
        System.out.println("Runtime Data Loader gestartet");
        startDataLoader();

        try {
            while (isRunning) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Runtime Data Loader wurde gestoppt");
        } finally {
            stopDataLoader();
        }

        //neuen Kunden/Produkte anlegen
        Kunden newKunde = new Kunden();
        kundenDAO.save(newKunde);

        Produkte newProdukt = new Produkte();
        produkteDAO.save(newProdukt);

        Standort newStandort = new Standort();
        standortDAO.save(newStandort);

        Rabatt newRabatt = new Rabatt();
        rabattDAO.save(newRabatt);

        Lager newLager = new Lager();
        lagerDAO.save(newLager);


        //Kunde/Produkt löschen
        Kunden deleteKunde = new Kunden();
        kundenDAO.delete(deleteKunde);

        Produkte deleteProdukt = new Produkte();
        produkteDAO.delete(deleteProdukt);


        Standort deleteStandort = new Standort();
        standortDAO.delete((deleteStandort));

        Rabatt deleteRabatt = new Rabatt();
        rabattDAO.delete(deleteRabatt);

        Lager deleteLager = new Lager();
        lagerDAO.delete(deleteLager);


    }
}