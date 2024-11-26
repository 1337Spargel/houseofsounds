import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import Factory.Kunden;
import Factory.Produkte;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class Application {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static volatile boolean isRuntimeActive = false;

    public static void main(String[] args) throws IOException {
        // Starten des Dataloaders
        Main.startDataLoader();
        isRuntimeActive = true;

        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        // Endpunkt Kunden
        server.createContext("/api/items/kunden", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Kunden> kunden;


                if (!isRuntimeActive) {
                    Main.clearKundenList();

                }

                kunden = Main.getKundenList();

                if (kunden.isEmpty()) {
                    sendResponse(exchange, 404, "Keine Kunden-Daten verfügbar");
                } else {
                    System.out.println("Daten für Kunden-API: " + gson.toJson(kunden));
                    sendJsonResponse(exchange, 200, kunden);
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });

        server.createContext("/api/items/produkte", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Produkte> produkte;

                if (!isRuntimeActive) {
                    Main.clearProdukteList();
                    Main.clearKundenList();
                }

                produkte = Main.getProdukteList();

                if (produkte.isEmpty()) {
                    sendResponse(exchange, 404, "Keine Produkte-Daten verfügbar");
                } else {
                    System.out.println("Daten für Produkte-API: " + gson.toJson(produkte));
                    sendJsonResponse(exchange, 200, produkte);
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });

        //server.createContext("/api/runtime-status", exchange -> {
        //    if ("GET".equals(exchange.getRequestMethod())) {
        //        sendJsonResponse(exchange, 200, "{\"isActive\": " + isRuntimeActive + "}");
        //    } else {
        //        sendResponse(exchange, 405, "Method Not Allowed");
        //    }
        //});
//
        //server.createContext("/api/set-runtime-status", exchange -> {
        //    if ("POST".equals(exchange.getRequestMethod())) {
//
        //        String body = new String(exchange.getRequestBody().readAllBytes());
        //        isRuntimeActive = Boolean.parseBoolean(body);
//
        //        if (!isRuntimeActive) {
        //            Main.stopDataLoader();
        //        } else {
        //            Main.startDataLoader();
        //        }
//
        //        sendResponse(exchange, 200, "Runtime status updated to: " + isRuntimeActive);
        //    } else {
        //        sendResponse(exchange, 405, "Method Not Allowed");
        //    }
        //});

        server.setExecutor(null);
        server.start();
        System.out.println("REST-API läuft auf Port " + serverPort);
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, Object data) {
        try {
            String jsonResponse = gson.toJson(data);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);
            try (OutputStream output = exchange.getResponseBody()) {
                output.write(jsonResponse.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String responseText) {
        try {
            byte[] responseBytes = responseText.getBytes();
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream output = exchange.getResponseBody()) {
                output.write(responseBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }
}