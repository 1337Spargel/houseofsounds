import Factory.Application;
import Factory.Kunden;

public class Main {
    public static void main(String[] args) {
        // Globale Anwendungsinstanz abrufen
        Application app = Application.getGlobalInstance();

        // Main aktiviert den Server und setzt die Tabelle
        app.setMainRunning(true);
        app.setCurrentTable(Kunden.class);

        System.out.println("Main läuft und hat Daten freigegeben.");
        System.out.println("Tabelle gesetzt: " + Kunden.class.getSimpleName());
        System.out.println("Status: Daten sind verfügbar");

        try {
            // Lange Laufzeit, damit der Prozess aktiv bleibt
            // Gibt Ihnen Zeit, die Application zu starten
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}