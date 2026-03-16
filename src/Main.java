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
import java.util.*;

class Service{
    String name;
    double cost;

    Service(String n,double c){
        name=n;
        cost=c;
    }
}

class AddOnServiceManager{

    private Map<String,List<Service>> reservationServices = new HashMap<>();

    public void addService(String id,Service s){
        reservationServices.putIfAbsent(id,new ArrayList<>());
        reservationServices.get(id).add(s);
        System.out.println(s.name+" added.");
    }

    public void showServices(String id){
        List<Service> services = reservationServices.get(id);
        if(services==null){
            System.out.println("No services selected.");
            return;
        }

        for(Service s:services){
            System.out.println(s.name+" ₹"+s.cost);
        }
    }

    public double calculateCost(String id){
        List<Service> services = reservationServices.get(id);
        if(services==null) return 0;

        double total=0;
        for(Service s:services) total+=s.cost;
        return total;
    }
}

public class UseCase7AddOnServiceSelection{

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.print("Reservation ID: ");
        String id = sc.nextLine();

        manager.addService(id,new Service("Breakfast",500));
        manager.addService(id,new Service("Extra Bed",800));

        manager.showServices(id);

        System.out.println("Total Cost ₹"+manager.calculateCost(id));
    }
}
import java.util.*;

class ReservationUC8{

    String id,name,room;
    int nights;

    ReservationUC8(String i,String n,String r,int ni){
        id=i;
        name=n;
        room=r;
        nights=ni;
    }

    void display(){
        System.out.println(id+" "+name+" "+room+" "+nights);
    }
}

class BookingHistory{

    List<ReservationUC8> history = new ArrayList<>();

    void addReservation(ReservationUC8 r){
        history.add(r);
    }

    List<ReservationUC8> getAll(){
        return history;
    }
}

class BookingReportService{

    void generateReport(List<ReservationUC8> list){

        System.out.println("\nBooking Report");

        for(ReservationUC8 r:list){
            r.display();
        }

        System.out.println("Total Bookings "+list.size());
    }
}

public class UseCase8BookingHistoryReport{

    public static void main(String[] args){

        BookingHistory history = new BookingHistory();

        history.addReservation(new ReservationUC8("1","Alice","Single",2));
        history.addReservation(new ReservationUC8("2","Bob","Double",3));

        BookingReportService service = new BookingReportService();

        service.generateReport(history.getAll());
    }
}
import java.util.*;

class InvalidBookingException extends Exception{
    InvalidBookingException(String msg){
        super(msg);
    }
}

class RoomInventoryUC9{

    Map<String,Integer> inventory = new HashMap<>();

    RoomInventoryUC9(){
        inventory.put("Single",5);
        inventory.put("Double",3);
        inventory.put("Suite",2);
    }

    void validate(String type) throws InvalidBookingException{
        if(!inventory.containsKey(type))
            throw new InvalidBookingException("Invalid Room Type");
    }
}

public class UseCase9ErrorHandlingValidation{

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        RoomInventoryUC9 inventory = new RoomInventoryUC9();

        try{

            System.out.print("Room Type: ");
            String type = sc.nextLine();

            inventory.validate(type);

            System.out.println("Booking Valid");

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
import java.util.*;

class ReservationUC10{

    String id,name,type;
    boolean cancelled=false;

    ReservationUC10(String i,String n,String t){
        id=i;
        name=n;
        type=t;
    }
}

class CancellationService{

    Map<String,ReservationUC10> reservations;
    Stack<String> rollback = new Stack<>();

    CancellationService(Map<String,ReservationUC10> r){
        reservations=r;
    }

    void cancel(String id){

        ReservationUC10 r = reservations.get(id);

        if(r==null){
            System.out.println("Reservation not found");
            return;
        }

        r.cancelled=true;
        rollback.push(id);

        System.out.println("Booking cancelled");
    }
}

public class UseCase10BookingCancellation{

    public static void main(String[] args){

        Map<String,ReservationUC10> map = new HashMap<>();

        map.put("1",new ReservationUC10("1","Alice","Single"));

        CancellationService service = new CancellationService(map);

        service.cancel("1");
    }
}
import java.util.*;

class BookingRequest{
    String guest;
    String room;

    BookingRequest(String g,String r){
        guest=g;
        room=r;
    }
}

class RoomInventoryUC11{

    Map<String,Integer> inventory = new HashMap<>();

    RoomInventoryUC11(){
        inventory.put("Single",2);
    }

    synchronized void allocate(String room,String guest){

        int available = inventory.get(room);

        if(available>0){
            inventory.put(room,available-1);
            System.out.println(Thread.currentThread().getName()+" booked for "+guest);
        }else{
            System.out.println("No room for "+guest);
        }
    }
}

class BookingProcessor extends Thread{

    Queue<BookingRequest> queue;
    RoomInventoryUC11 inventory;

    BookingProcessor(Queue<BookingRequest> q,RoomInventoryUC11 i){
        queue=q;
        inventory=i;
    }

    public void run(){

        while(!queue.isEmpty()){

            BookingRequest r = queue.poll();
            if(r!=null)
                inventory.allocate(r.room,r.guest);
        }
    }
}

public class UseCase11ConcurrentBookingSimulation{

    public static void main(String[] args){

        Queue<BookingRequest> queue = new LinkedList<>();

        queue.add(new BookingRequest("Alice","Single"));
        queue.add(new BookingRequest("Bob","Single"));

        RoomInventoryUC11 inventory = new RoomInventoryUC11();

        new BookingProcessor(queue,inventory).start();
        new BookingProcessor(queue,inventory).start();
    }
}
import java.io.*;
        import java.util.*;

class ReservationUC12 implements Serializable{

    String id,name,type;

    ReservationUC12(String i,String n,String t){
        id=i;
        name=n;
        type=t;
    }
}

class SystemState implements Serializable{

    Map<String,Integer> inventory;
    List<ReservationUC12> bookings;

    SystemState(Map<String,Integer> i,List<ReservationUC12> b){
        inventory=i;
        bookings=b;
    }
}

class PersistenceService{

    private static final String FILE="state.dat";

    void save(SystemState state){

        try{
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(FILE));

            out.writeObject(state);
            out.close();

            System.out.println("State saved");
        }
        catch(Exception e){
            System.out.println("Save error");
        }
    }

    SystemState load(){

        try{
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(FILE));

            SystemState state = (SystemState) in.readObject();
            in.close();

            return state;
        }
        catch(Exception e){
            System.out.println("No previous state");
            return null;
        }
    }
}

public class UseCase12DataPersistenceRecovery{

    public static void main(String[] args){

        PersistenceService service = new PersistenceService();

        Map<String,Integer> inventory = new HashMap<>();
        List<ReservationUC12> bookings = new ArrayList<>();

        SystemState state = service.load();

        if(state!=null){
            inventory=state.inventory;
            bookings=state.bookings;
        }
        else{
            inventory.put("Single",5);
        }

        bookings.add(new ReservationUC12("1","Alice","Single"));
        inventory.put("Single",inventory.get("Single")-1);

        service.save(new SystemState(inventory,bookings));

        System.out.println("System running with persistence.");
    }
}