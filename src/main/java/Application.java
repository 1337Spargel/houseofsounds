import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import Factory.*;
import com.fasterxml.jackson.databind.PropertyMetadata;
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
                    Main.clearProdukteList();
                    Main.clearlagerlist();
                    Main.clearrabattList();
                    Main.clearstandortList();
                }

                kunden = Main.getKundenList();


                if (kunden.isEmpty()) {
                    sendResponse(exchange, 404, "Keine Kunden-Daten verfügbar");
                } else {
                    System.out.println("Daten für Kunden-API: " + gson.toJson(kunden));
                    sendJsonResponse(exchange, 200, kunden);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());
                    Kunden newKunde = gson.fromJson(requestBody, Kunden.class);
                    Main.kundenDAO.save(newKunde);

                    sendJsonResponse(exchange, 201, "Kunde erfolgreich hinzugefügt");

                } catch (Exception e) {
                    sendResponse(exchange, 400, "Fehler beim Hinzufügen des Kunden: " + e.getMessage());

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
                    Main.clearlagerlist();
                    Main.clearrabattList();
                    Main.clearstandortList();
                }

                produkte = Main.getProdukteList();

                if (produkte.isEmpty()) {
                    sendResponse(exchange, 404, "Keine Produkte-Daten verfügbar");
                } else {
                    System.out.println("Daten für Produkte-API: " + gson.toJson(produkte));
                    sendJsonResponse(exchange, 200, produkte);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());
                    Produkte newProdukte = gson.fromJson(requestBody, Produkte.class);
                    Main.produkteDAO.save(newProdukte);

                    sendJsonResponse(exchange, 201, "Produkt erfolgreich hinzugefügt");

                } catch (Exception e) {
                    sendResponse(exchange, 400, "Fehler beim Hinzufügen des Produktes: " + e.getMessage());

                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });

        server.createContext("/api/items/lager", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Lager> lager;

                if (!isRuntimeActive) {
                    Main.clearProdukteList();
                    Main.clearKundenList();
                    Main.clearlagerlist();
                    Main.clearrabattList();
                    Main.clearstandortList();
                }

                lager = Main.getLagerList();

                if (lager.isEmpty()) {
                    sendResponse(exchange, 404, "Keine Lager-Daten verfügbar");
                } else {
                    System.out.println("Daten für Lager-API: " + gson.toJson(lager));
                    sendJsonResponse(exchange, 200, lager);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());
                    Lager newLager = gson.fromJson(requestBody, Lager.class);
                    Main.lagerDAO.save(newLager);

                    sendJsonResponse(exchange, 201, "Lager erfolgreich hinzugefügt");

                } catch (Exception e) {
                    sendResponse(exchange, 400, "Fehler beim Hinzufügen des Lagers: " + e.getMessage());

                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });

        server.createContext("/api/items/standort", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Standort> standort;

                if (!isRuntimeActive) {
                    Main.clearProdukteList();
                    Main.clearKundenList();
                    Main.clearlagerlist();
                    Main.clearrabattList();
                    Main.clearstandortList();
                }

                standort = Main.getStandortList();

                if (standort.isEmpty()) {
                    sendResponse(exchange, 404, "Keine Standort-Daten verfügbar");
                } else {
                    System.out.println("Daten für Standort-API: " + gson.toJson(standort));
                    sendJsonResponse(exchange, 200, standort);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());
                    Standort newStandort = gson.fromJson(requestBody, Standort.class);
                    Main.standortDAO.save(newStandort);

                    sendJsonResponse(exchange, 201, "Standort erfolgreich hinzugefügt");

                } catch (Exception e) {
                    sendResponse(exchange, 400, "Fehler beim Hinzufügen des Standorts: " + e.getMessage());

                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });

        server.createContext("/api/items/rabatt", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Rabatt> rabatt;

                if (!isRuntimeActive) {
                    Main.clearProdukteList();
                    Main.clearKundenList();
                    Main.clearlagerlist();
                    Main.clearrabattList();
                    Main.clearstandortList();
                }

                rabatt = Main.getRabattList();

                if (rabatt.isEmpty()) {
                    sendResponse(exchange, 404, "Keine Rabatt-Daten verfügbar");
                } else {
                    System.out.println("Daten für Rabatt-API: " + gson.toJson(rabatt));
                    sendJsonResponse(exchange, 200, rabatt);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());
                    Rabatt newRabatt = gson.fromJson(requestBody, Rabatt.class);
                    Main.rabattDAO.save(newRabatt);

                    sendJsonResponse(exchange, 201, "Rabatt erfolgreich hinzugefügt");

                } catch (Exception e) {
                    sendResponse(exchange, 400, "Fehler beim Hinzufügen des Rabattes: " + e.getMessage());

                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });

        server.createContext("/api/items/kunden/update", exchange -> {
            if ("PUT".equals(exchange.getRequestMethod())) {
                try {
                    // Request Body auslesen
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    // JSON in Kunden-Objekt umwandeln
                    Kunden updateKunde = gson.fromJson(requestBody, Kunden.class);

                    // Überprüfen, ob eine ID vorhanden ist
                    if (updateKunde.getKunden_ID() == -1) {
                        sendResponse(exchange, 400, "Keine Kunden-ID für Update angegeben");
                        return;
                    }

                    // Versuch des Updates über DAO
                    Main.kundenDAO.update(updateKunde);


                    sendJsonResponse(exchange, 200, "Kunde erfolgreich aktualisiert");

                } catch (Exception e) {
                    sendResponse(exchange, 500, "Fehler beim Update des Kunden: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });

        server.createContext("/api/items/produkte/update", exchange -> {
            if ("PUT".equals(exchange.getRequestMethod())) {
                try {
                    // Request Body auslesen
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    // JSON in Produkte-Objekt umwandeln
                    Produkte updateProdukt = gson.fromJson(requestBody, Produkte.class);

                    // Überprüfen, ob eine ID vorhanden ist
                    if (updateProdukt.getProdukte_ID() == -1) {
                        sendResponse(exchange, 400, "Keine Produkt-ID für Update angegeben");
                        return;
                    }

                    // Versuch des Updates über DAO
                    Main.produkteDAO.update(updateProdukt);

                    sendJsonResponse(exchange, 200, "Produkt erfolgreich aktualisiert");

                } catch (Exception e) {
                    sendResponse(exchange, 500, "Fehler beim Update des Produkts: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });


        server.createContext("/api/items/standort/update", exchange -> {
            if ("PUT".equals(exchange.getRequestMethod())) {
                try {
                    // Request Body auslesen
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    // JSON in Produkte-Objekt umwandeln
                    Standort updateStandort = gson.fromJson(requestBody, Standort.class);

                    // Überprüfen, ob eine ID vorhanden ist
                    if (updateStandort.getStandort_ID() == -1) {
                        sendResponse(exchange, 400, "Keine Standort-ID für Update angegeben");
                        return;
                    }

                    // Versuch des Updates über DAO
                    Main.standortDAO.update(updateStandort);

                    sendJsonResponse(exchange, 200, "Standort erfolgreich aktualisiert");

                } catch (Exception e) {
                    sendResponse(exchange, 500, "Fehler beim Update des Standortes: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });

        server.createContext("/api/items/rabatt/update", exchange -> {
            if ("PUT".equals(exchange.getRequestMethod())) {
                try {
                    // Request Body auslesen
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    // JSON in Produkte-Objekt umwandeln
                    Rabatt updateRabatt = gson.fromJson(requestBody, Rabatt.class);

                    // Überprüfen, ob eine ID vorhanden ist
                    if (updateRabatt.getRabatt_ID() == -1) {
                        sendResponse(exchange, 400, "Keine Rabatt-ID für Update angegeben");
                        return;
                    }

                    // Versuch des Updates über DAO
                    Main.rabattDAO.update(updateRabatt);

                    sendJsonResponse(exchange, 200, "Rabatt erfolgreich aktualisiert");

                } catch (Exception e) {
                    sendResponse(exchange, 500, "Fehler beim Update des Rabattes: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });

        server.createContext("/api/items/lager/update", exchange -> {
            if ("PUT".equals(exchange.getRequestMethod())) {
                try {
                    // Request Body auslesen
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    // JSON in Produkte-Objekt umwandeln
                    Lager updateLager = gson.fromJson(requestBody, Lager.class);

                    // Überprüfen, ob eine ID vorhanden ist
                    if (updateLager.getLager_ID() == null) {
                        sendResponse(exchange, 400, "Keine Lager-ID für Update angegeben");
                        return;
                    }

                    // Versuch des Updates über DAO
                    Main.lagerDAO.update(updateLager);

                    sendJsonResponse(exchange, 200, "Lager erfolgreich aktualisiert");

                } catch (Exception e) {
                    sendResponse(exchange, 500, "Fehler beim Update des Lagers: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });


        server.createContext("/api/items/kunden/delete", exchange -> {
            if ("DELETE".equals(exchange.getRequestMethod())) {
                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    Kunden deleteKunde = gson.fromJson(requestBody, Kunden.class);

                    if (deleteKunde.getKunden_ID() == -1) {
                        sendResponse(exchange, 400, "Keine Kunden-ID für Delete angegeben");
                        return;
                    }

                    Main.kundenDAO.delete(deleteKunde);

                    sendJsonResponse(exchange, 200, "Kunde erfolgreich gelöscht");

                } catch (Exception e) {
                    sendResponse(exchange, 500, "Fehler beim Löschen des Kunden: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method not allowed");
            }
        });

        server.createContext("/api/items/produkte/delete", exchange -> {
            if ("DELETE".equals(exchange.getRequestMethod())) {

                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    Produkte deleteProdukt = gson.fromJson(requestBody, Produkte.class);

                    if (deleteProdukt.getProdukte_ID() == -1) {
                        sendResponse(exchange, 400, "Keine Produkt-ID für Delete angegeben");
                        return;
                    }

                    Main.produkteDAO.delete(deleteProdukt);

                    sendJsonResponse(exchange, 200, "Produkt erfolgreich gelöscht");
                } catch (Exception e) {
                    sendJsonResponse(exchange, 500, "Fehler beim Löschen des Produkt: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method not allowed");
            }

        });

        server.createContext("/api/items/standort/delete", exchange -> {
            if ("DELETE".equals(exchange.getRequestMethod())) {

                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    Standort deleteStandort = gson.fromJson(requestBody, Standort.class);

                    if (deleteStandort.getStandort_ID() == -1) {
                        sendResponse(exchange, 400, "Keine Standort-ID für Delete angegeben");
                        return;
                    }

                    Main.standortDAO.delete(deleteStandort);

                    sendJsonResponse(exchange, 200, "Standort erfolgreich gelöscht");
                } catch (Exception e) {
                    sendJsonResponse(exchange, 500, "Fehler beim Löschen des Standorts: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method not allowed");
            }

        });

        server.createContext("/api/items/rabatt/delete", exchange -> {
            if ("DELETE".equals(exchange.getRequestMethod())) {

                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    Rabatt deleteRabatt = gson.fromJson(requestBody, Rabatt.class);

                    if (deleteRabatt.getRabatt_ID() == -1) {
                        sendResponse(exchange, 400, "Keine Rabatt-ID für Delete angegeben");
                        return;
                    }

                    Main.rabattDAO.delete(deleteRabatt);

                    sendJsonResponse(exchange, 200, "Rabatt erfolgreich gelöscht");
                } catch (Exception e) {
                    sendJsonResponse(exchange, 500, "Fehler beim Löschen des Rabattes: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method not allowed");
            }

        });

        server.createContext("/api/items/lager/delete", exchange -> {
            if ("DELETE".equals(exchange.getRequestMethod())) {

                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    Lager deleteLager = gson.fromJson(requestBody, Lager.class);

                    if (deleteLager.getLager_ID() == null) {
                        sendResponse(exchange, 400, "Keine Lager-ID für Delete angegeben");
                        return;
                    }

                    Main.lagerDAO.delete(deleteLager);

                    sendJsonResponse(exchange, 200, "Lager erfolgreich gelöscht");
                } catch (Exception e) {
                    sendJsonResponse(exchange, 500, "Fehler beim Löschen des Lagers: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method not allowed");
            }

        });


        server.createContext("/api/items/{type}/{id}", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                try {
                    // Extrahiere den Entitätstyp (z.B. "kunden" oder "produkte") aus der URL
                    String type = exchange.getRequestURI().getPath().split("/")[3];
                    int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[4]);

                    // Den passenden DAO anhand des Entitätstyps finden
                    Object entity = null;
                    if ("kunden".equalsIgnoreCase(type)) {
                        // Dynamisch die Methode getById aus dem KundenDAO aufrufen
                        entity = Main.kundenDAO.getById(id);
                    } else if ("produkte".equalsIgnoreCase(type)) {
                        // Dynamisch die Methode getById aus dem ProdukteDAO aufrufen
                        entity = Main.produkteDAO.getById(id);
                    }

                    if (entity != null) {
                        sendJsonResponse(exchange, 200, entity);
                    } else {
                        sendResponse(exchange, 404, "Entität nicht gefunden");
                    }

                } catch (NumberFormatException e) {
                    sendResponse(exchange, 400, "Ungültige ID");
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });


        server.setExecutor(null);
        server.start();
        System.out.println("REST-API läuft auf Port " + serverPort);

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