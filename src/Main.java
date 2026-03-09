import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a booking request
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Room Requested: " + roomType);
    }
}

// Booking request queue
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add a booking request
    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // Display all queued requests in arrival order
    public void displayQueue() {
        System.out.println("\n===== Current Booking Requests =====");
        if(requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
        } else {
            for (Reservation r : requestQueue) {
                r.displayReservation();
            }
        }
    }

    // Remove a request from the queue (used later when allocating rooms)
    public Reservation processNextRequest() {
        return requestQueue.poll(); // returns null if queue is empty
    }
}

// Main program
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        // Initialize booking request queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate guest booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests to the queue
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queued requests in FIFO order
        bookingQueue.displayQueue();
    }
}