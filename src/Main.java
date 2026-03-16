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

}import java.util.*;

abstract class Room {
    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single Room",1,100); }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room",2,180); }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite Room",3,300); }
}

class RoomInventoryUC6 {

    private HashMap<String,Integer> inventory = new HashMap<>();

    public RoomInventoryUC6(){
        inventory.put("Single Room",5);
        inventory.put("Double Room",3);
        inventory.put("Suite Room",2);
    }

    public int getAvailability(String type){
        return inventory.getOrDefault(type,0);
    }

    public boolean allocateRoom(String type){
        int available = inventory.getOrDefault(type,0);
        if(available>0){
            inventory.put(type,available-1);
            return true;
        }
        return false;
    }

    public void display(){
        System.out.println("\nInventory");
        inventory.forEach((k,v)-> System.out.println(k+" : "+v));
    }
}

class ReservationUC6{
    String guestName;
    String roomType;

    ReservationUC6(String g,String r){
        guestName=g;
        roomType=r;
    }
}

class BookingServiceUC6{

    private RoomInventoryUC6 inventory;

    BookingServiceUC6(RoomInventoryUC6 inv){
        inventory=inv;
    }

    public void confirmReservation(ReservationUC6 r){
        if(inventory.allocateRoom(r.roomType)){
            System.out.println("Booking confirmed for "+r.guestName);
        }else{
            System.out.println("No room available for "+r.guestName);
        }
    }
}

public class UseCase6RoomAllocationService{

    public static void main(String[] args){

        RoomInventoryUC6 inventory = new RoomInventoryUC6();

        Queue<ReservationUC6> queue = new LinkedList<>();

        queue.add(new ReservationUC6("Alice","Single Room"));
        queue.add(new ReservationUC6("Bob","Double Room"));
        queue.add(new ReservationUC6("Charlie","Suite Room"));

        BookingServiceUC6 service = new BookingServiceUC6(inventory);

        while(!queue.isEmpty()){
            service.confirmReservation(queue.poll());
        }

        inventory.display();
    }
}