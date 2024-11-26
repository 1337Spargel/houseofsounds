import Factory.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Informieren, dass Main startet
        Application.setMainRunning(true);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welche Tabelle möchten Sie anzeigen?");
            System.out.println("1. Lager");
            System.out.println("2. PLZ");
            System.out.println("3. Produkte");
            System.out.println("4. Rabatt");
            System.out.println("5. Kunden");
            System.out.println("6. Standort");
            System.out.println("7. Beenden");
            System.out.print("Bitte wählen Sie eine Option: ");

            int choice = scanner.nextInt();
            if (choice == 7) {
                running = false;
            } else {
                Class<?> clazz = null;
                switch (choice) {
                    case 1:
                        clazz = Lager.class;
                        break;
                    case 2:
                        clazz = PLZ.class;
                        break;
                    case 3:
                        clazz = Produkte.class;
                        break;
                    case 4:
                        clazz = Rabatt.class;
                        break;
                    case 5:
                        clazz = Kunden.class;
                        break;
                    case 6:
                        clazz = Standort.class;
                        break;
                    default:
                        System.out.println("Ungültige Auswahl. Bitte versuchen Sie es erneut.");
                        continue;
                }
                // Setzen der aktuellen Tabelle vor dem Drucken
                Application.setCurrentTable(clazz);
                printTable(clazz);
            }
        }

        // Informieren, dass Main beendet wird
        Application.setMainRunning(false);
        scanner.close();
    }

    private static <T> void printTable(Class<T> clazz) {
        GenericDAO<T> dao = DAOFactory.getDAO(clazz);
        List<T> records = dao.getAll();

        for (T record : records) {
            System.out.println(record.toString());
            System.out.println("---------------------------");
        }
    }
}