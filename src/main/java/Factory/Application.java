package Factory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

public class Application {

    private static Application globalInstance;

    public static Application getGlobalInstance() {
        if (globalInstance == null) {
            globalInstance = new Application();
        }
        return globalInstance;
    }

    public Application() {
        globalInstance = this;
    }

    // Hauptmethode zum direkten Starten der Anwendung
    public static void main(String[] args) {
        Application app = getGlobalInstance();

        // ENTSCHEIDENDER PUNKT: Prüfen, ob Main läuft
        if (!isMainRunningCheck()) {
            System.err.println("FEHLER: Main ist nicht gestartet! Starten Sie zuerst Main.java");
            System.err.println("Bedinungen für Datenzugriff nicht erfüllt:");
            System.err.println("1. Main.java muss zuerst gestartet werden");
            System.err.println("2. Main muss isMainRunning auf true setzen");
            System.err.println("3. Main muss eine Tabelle setzen");
            return;
        }

        try {
            app.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Statische Methode zur Überprüfung, ob Main läuft
    private static boolean isMainRunningCheck() {
        try {
            // Kurze Wartezeit, falls Main gerade startet
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Globale Instanz abrufen und Status prüfen
        Application app = getGlobalInstance();
        return app.isMainRunning() && app.getCurrentTable() != null;
    }

    public void startServer() throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        server.createContext("/api/data", (exchange -> {
            // NOCHMALIGE Sicherheitsprüfung
            if (!isMainRunningCheck()) {
                String errorResponse = "FEHLER: Main läuft nicht! Starten Sie zuerst Main.java";
                exchange.sendResponseHeaders(403, errorResponse.getBytes().length);

                try (OutputStream output = exchange.getResponseBody()) {
                    output.write(errorResponse.getBytes());
                }
                exchange.close();
                return;
            }

            // Daten nur ausgeben, wenn Main läuft
            List<String> dataList = List.of("Datensatz 1", "Datensatz 2", "Datensatz 3");
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(dataList);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(jsonResponse.getBytes());
            }
            exchange.close();
        }));

        server.setExecutor(null);
        server.start();
        System.out.println("Server läuft auf Port " + serverPort);
        System.out.println("WICHTIG: Daten werden NUR angezeigt, wenn Main.java läuft!");
    }

    // Synchronisierte Getter und Setter
    public synchronized void setMainRunning(boolean running) {
        isMainRunning = running;
    }

    public synchronized boolean isMainRunning() {
        return isMainRunning;
    }

    public synchronized void setCurrentTable(Class<?> table) {
        currentSelectedTable = table;
    }

    public synchronized Class<?> getCurrentTable() {
        return currentSelectedTable;
    }

    // Zustandsvariablen
    private boolean isMainRunning = false;
    private Class<?> currentSelectedTable = null;
}
