package Factory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

public class Application {
    // Statische Variable, um den Ausführungsstatus zu tracken
    private static boolean isMainRunning = false;
    // Variable für die aktuell ausgewählte Tabelle
    private static Class<?> currentSelectedTable = null;

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        server.createContext("/api/data", (exchange -> {
            if (isMainRunning && currentSelectedTable != null) {
                GenericDAO<?> dao = DAOFactory.getDAO(currentSelectedTable);
                List<?> dataList = dao.getAll();

                Gson gson = new Gson();
                String jsonResponse = gson.toJson(dataList);

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

                OutputStream output = exchange.getResponseBody();
                output.write(jsonResponse.getBytes());
                output.flush();
                exchange.close();
            } else {
                String errorResponse = "Es geht nix";
                exchange.sendResponseHeaders(403, errorResponse.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(errorResponse.getBytes());
                output.flush();
                exchange.close();
            }
        }));

        server.setExecutor(null);
        server.start();
        System.out.println("Server läuft auf " + serverPort);
    }

    // Methode zum Setzen des Laufzeitstatus
    public static void setMainRunning(boolean running) {
        isMainRunning = running;
    }

    // Methode zum Aktualisieren der ausgewählten Tabelle
    public static void setCurrentTable(Class<?> table) {
        currentSelectedTable = table;
    }
}