import java.io.*;
import java.util.*;

/* =========================
   ROOM MODEL
========================= */

class Room {

    String type;
    int beds;
    double price;

    Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    void display() {
        System.out.println(type + " | Beds: " + beds + " | Price: $" + price);
    }
}

/* =========================
   INVENTORY
========================= */

class Inventory {

    Map<String, Integer> rooms = new HashMap<>();

    Inventory() {
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    synchronized boolean bookRoom(String type) {

        int available = rooms.getOrDefault(type, 0);

        if (available > 0) {
            rooms.put(type, available - 1);
            return true;
        }

        return false;
    }

    synchronized void releaseRoom(String type) {
        rooms.put(type, rooms.get(type) + 1);
    }

    void showInventory() {

        System.out.println("\nInventory");

        for (String key : rooms.keySet()) {
            System.out.println(key + " Rooms: " + rooms.get(key));
        }
    }
}

/* =========================
   RESERVATION
========================= */

class Reservation implements Serializable {

    String id;
    String guest;
    String roomType;
    int nights;
    boolean cancelled = false;

    Reservation(String id, String guest, String roomType, int nights) {
        this.id = id;
        this.guest = guest;
        this.roomType = roomType;
        this.nights = nights;
    }

    void display() {
        System.out.println(id + " | " + guest + " | " + roomType + " | Nights: " + nights + " | Cancelled: " + cancelled);
    }
}

/* =========================
   ADD ON SERVICES
========================= */

class Service {

    String name;
    double price;

    Service(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class AddOnManager {

    Map<String, List<Service>> services = new HashMap<>();

    void addService(String reservationId, Service service) {

        services.putIfAbsent(reservationId, new ArrayList<>());

        services.get(reservationId).add(service);

        System.out.println(service.name + " added.");
    }

    void showServices(String reservationId) {

        List<Service> list = services.get(reservationId);

        if (list == null) {
            System.out.println("No services.");
            return;
        }

        double total = 0;

        for (Service s : list) {
            System.out.println(s.name + " ₹" + s.price);
            total += s.price;
        }

        System.out.println("Total Add-On Cost ₹" + total);
    }
}

/* =========================
   PERSISTENCE
========================= */

class SystemState implements Serializable {

    Map<String, Integer> inventory;
    List<Reservation> reservations;

    SystemState(Map<String, Integer> inv, List<Reservation> res) {
        inventory = inv;
        reservations = res;
    }
}

class PersistenceService {

    private static final String FILE = "hotel_state.dat";

    void save(SystemState state) {

        try {

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE));

            out.writeObject(state);

            out.close();

            System.out.println("System saved.");

        } catch (Exception e) {
            System.out.println("Save failed.");
        }
    }

    SystemState load() {

        try {

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE));

            SystemState state = (SystemState) in.readObject();

            in.close();

            System.out.println("System restored.");

            return state;

        } catch (Exception e) {

            System.out.println("No previous state found.");

            return null;
        }
    }
}

/* =========================
   MAIN PROGRAM
========================= */

public class Main {

    static Inventory inventory = new Inventory();
    static Map<String, Reservation> reservations = new HashMap<>();
    static AddOnManager addOnManager = new AddOnManager();
    static PersistenceService persistence = new PersistenceService();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        SystemState state = persistence.load();

        if (state != null) {

            inventory.rooms = state.inventory;

            for (Reservation r : state.reservations) {
                reservations.put(r.id, r);
            }
        }

        int choice;

        do {

            System.out.println("\n====== BOOK MY STAY ======");

            System.out.println("1. View Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. View Reservations");
            System.out.println("4. Add Service");
            System.out.println("5. View Services");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Show Inventory");
            System.out.println("8. Simulate Concurrent Booking");
            System.out.println("9. Save System");
            System.out.println("10. Exit");

            System.out.print("Choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    new Room("Single", 1, 100).display();
                    new Room("Double", 2, 180).display();
                    new Room("Suite", 3, 300).display();

                    break;

                case 2:

                    System.out.print("Reservation ID: ");
                    String id = sc.nextLine();

                    System.out.print("Guest Name: ");
                    String name = sc.nextLine();

                    System.out.print("Room Type (Single/Double/Suite): ");
                    String type = sc.nextLine();

                    System.out.print("Nights: ");
                    int nights = sc.nextInt();

                    if (inventory.bookRoom(type)) {

                        Reservation r = new Reservation(id, name, type, nights);

                        reservations.put(id, r);

                        System.out.println("Booking Confirmed");

                    } else {

                        System.out.println("Room Not Available");
                    }

                    break;

                case 3:

                    for (Reservation r : reservations.values()) {
                        r.display();
                    }

                    break;

                case 4:

                    System.out.print("Reservation ID: ");
                    String rid = sc.nextLine();

                    addOnManager.addService(rid, new Service("Breakfast", 500));

                    break;

                case 5:

                    System.out.print("Reservation ID: ");
                    String sid = sc.nextLine();

                    addOnManager.showServices(sid);

                    break;

                case 6:

                    System.out.print("Reservation ID to cancel: ");
                    String cid = sc.nextLine();

                    Reservation r = reservations.get(cid);

                    if (r != null && !r.cancelled) {

                        r.cancelled = true;

                        inventory.releaseRoom(r.roomType);

                        System.out.println("Booking Cancelled");

                    } else {
                        System.out.println("Invalid Reservation");
                    }

                    break;

                case 7:

                    inventory.showInventory();

                    break;

                case 8:

                    System.out.println("Simulating concurrent booking...");

                    Thread t1 = new Thread(() -> inventory.bookRoom("Single"));
                    Thread t2 = new Thread(() -> inventory.bookRoom("Single"));

                    t1.start();
                    t2.start();

                    break;

                case 9:

                    List<Reservation> list = new ArrayList<>(reservations.values());

                    persistence.save(new SystemState(inventory.rooms, list));

                    break;

                case 10:

                    System.out.println("Exiting...");

                    break;

                default:

                    System.out.println("Invalid choice");
            }

        } while (choice != 10);

        sc.close();
    }
}