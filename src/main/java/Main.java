import Factory.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Map<Integer, Class<?>> tableOptions = new HashMap<>();

    static {
        tableOptions.put(1, Lager.class);
        tableOptions.put(2, PLZ.class);
        tableOptions.put(3, Produkte.class);
        tableOptions.put(4, Rabatt.class);
        tableOptions.put(5, Kunden.class);
        tableOptions.put(6, Standort.class);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welche Tabelle möchten Sie anzeigen?");
            tableOptions.forEach((key, value) -> System.out.println(key + ". " + value.getSimpleName()));
            System.out.println((tableOptions.size() + 1) + ". Beenden");
            System.out.print("Bitte wählen Sie eine Option: ");

            int choice = scanner.nextInt();
            if (choice == tableOptions.size() + 1) {
                running = false;
            } else {
                Class<?> clazz = tableOptions.get(choice);
                if (clazz != null) {
                    printTable(clazz);
                } else {
                    System.out.println("Ungültige Auswahl. Bitte versuchen Sie es erneut.");
                }
            }
        }
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
