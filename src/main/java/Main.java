import Factory.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    private static final CopyOnWriteArrayList<Kunden> kundenList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Produkte> produkteList = new CopyOnWriteArrayList<>();

    private static final GenericDAO<Kunden> kundenDAO = DAOFactory.getDAO(Kunden.class);
    private static final GenericDAO<Produkte> produkteDAO = DAOFactory.getDAO(Produkte.class);

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

                    kundenList.addAll(kundenDAO.getAll());
                    produkteList.addAll(produkteDAO.getAll());

                    System.out.println("Daten aktualisiert: " +
                            kundenList.size() + " Kunden, " +
                            produkteList.size() + " Produkte");

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

    public static List<Kunden> getKundenList() {
        return kundenList;
    }

    public static List<Produkte> getProdukteList() {
        return produkteList;
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
    }
}