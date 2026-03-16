import java.util.*;

// Room domain classes
abstract class Room {
    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: $" + price);
    }

    public String getRoomType() {
        return roomType;
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 300);
    }
}

// Centralized inventory
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Check availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Decrement inventory
    public boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\n===== Current Inventory =====");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}

// Booking request
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// Booking queue
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation r) {
        requestQueue.add(r);
        System.out.println("Booking request added for " + r.getGuestName());
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}

// Allocation service
class BookingService {

    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRoomIDs;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRoomIDs = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomID(String roomType) {
        Set<String> ids = allocatedRoomIDs.getOrDefault(roomType, new HashSet<>());
        int id = ids.size() + 1;
        String roomID = roomType.substring(0,1) + String.format("%03d", id);
        ids.add(roomID);
        allocatedRoomIDs.put(roomType, ids);
        return roomID;
    }

    // Process booking request
    public void confirmReservation(Reservation reservation) {
        String roomType = reservation.getRoomType();
        if(inventory.getAvailability(roomType) > 0) {
            // Allocate inventory
            inventory.allocateRoom(roomType);

            // Generate unique room ID
            String roomID = generateRoomID(roomType);

            System.out.println("Reservation Confirmed: " + reservation.getGuestName() +
                    " | Room Type: " + roomType + " | Room ID: " + roomID);
        } else {
            System.out.println("Reservation Failed (No Availability): " + reservation.getGuestName() +
                    " | Room Type: " + roomType);
        }
    }
}

// Main program
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking queue and add requests
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));
        queue.addRequest(new Reservation("Charlie", "Suite Room"));
        queue.addRequest(new Reservation("Diana", "Single Room"));
        queue.addRequest(new Reservation("Eve", "Single Room"));

        // Initialize booking service
        BookingService service = new BookingService(inventory);

        // Process all queued requests
        System.out.println("\n===== Processing Bookings =====");
        while(!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            service.confirmReservation(r);
        }

        // Display remaining inventory
        inventory.displayInventory();
    }
}
import java.util.*;

/* Service class representing an optional add-on */
class Service {
    String name;
    double cost;

    Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }
}

/* Manager class handling add-on services */
class AddOnServiceManager {

    // Map reservationId -> List of services
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, Service service) {

        reservationServices.putIfAbsent(reservationId, new ArrayList<>());

        reservationServices.get(reservationId).add(service);

        System.out.println(service.name + " added to reservation " + reservationId);
    }

    // Display services for a reservation
    public void showServices(String reservationId) {

        List<Service> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        System.out.println("Selected Services:");

        for (Service s : services) {
            System.out.println("- " + s.name + " : ₹" + s.cost);
        }
    }

    // Calculate additional cost
    public double calculateCost(String reservationId) {

        List<Service> services = reservationServices.get(reservationId);

        if (services == null)
            return 0;

        double total = 0;

        for (Service s : services) {
            total += s.cost;
        }

        return total;
    }
}

/* Main class for UC7 */
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.print("Enter Reservation ID: ");
        String reservationId = sc.nextLine();

        int choice;

        do {

            System.out.println("\nAdd-On Services");
            System.out.println("1. Breakfast - ₹500");
            System.out.println("2. Airport Pickup - ₹1000");
            System.out.println("3. Extra Bed - ₹800");
            System.out.println("4. Show Selected Services");
            System.out.println("5. Show Total Add-On Cost");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    manager.addService(reservationId, new Service("Breakfast", 500));
                    break;

                case 2:
                    manager.addService(reservationId, new Service("Airport Pickup", 1000));
                    break;

                case 3:
                    manager.addService(reservationId, new Service("Extra Bed", 800));
                    break;

                case 4:
                    manager.showServices(reservationId);
                    break;

                case 5:
                    double cost = manager.calculateCost(reservationId);
                    System.out.println("Total Add-On Cost: ₹" + cost);
                    break;

                case 6:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice");

            }

        } while (choice != 6);

        sc.close();
    }
}
import java.util.*;

/* Reservation class representing a confirmed booking */
class Reservation {

    String reservationId;
    String guestName;
    String roomType;
    int nights;

    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Guest Name: " + guestName);
        System.out.println("Room Type: " + roomType);
        System.out.println("Nights: " + nights);
        System.out.println("----------------------------");
    }
}

/* BookingHistory stores reservations in order */
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation added to booking history.");
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

/* Report service generates summaries */
class BookingReportService {

    public void generateReport(List<Reservation> reservations) {

        if (reservations.isEmpty()) {
            System.out.println("No booking history available.");
            return;
        }

        System.out.println("\n===== BOOKING HISTORY REPORT =====");

        for (Reservation r : reservations) {
            r.displayReservation();
        }

        System.out.println("Total Bookings: " + reservations.size());
    }
}

/* Main class */
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        int choice;

        do {

            System.out.println("\n1. Confirm Booking");
            System.out.println("2. View Booking History");
            System.out.println("3. Generate Report");
            System.out.println("4. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Reservation ID: ");
                    String id = sc.nextLine();

                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Room Type: ");
                    String room = sc.nextLine();

                    System.out.print("Enter Nights: ");
                    int nights = sc.nextInt();

                    Reservation r = new Reservation(id, name, room, nights);

                    history.addReservation(r);
                    break;

                case 2:

                    List<Reservation> bookings = history.getAllReservations();

                    if (bookings.isEmpty()) {
                        System.out.println("No bookings found.");
                    } else {
                        for (Reservation res : bookings) {
                            res.displayReservation();
                        }
                    }
                    break;

                case 3:

                    reportService.generateReport(history.getAllReservations());
                    break;

                case 4:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice");

            }

        } while (choice != 4);

        sc.close();
    }
}