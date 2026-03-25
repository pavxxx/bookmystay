import java.util.*;

// Reservation (represents booking request)
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

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType;
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    // View all requests (read-only)
    public void viewRequests() {
        System.out.println("\nCurrent Booking Requests in Queue:");
        for (Reservation r : queue) {
            System.out.println(r);
        }
    }

    // Get next request (without removing - optional)
    public Reservation peekNext() {
        return queue.peek();
    }

    // Remove next request (for future UC)
    public Reservation processNext() {
        return queue.poll();
    }
}

// Main class
public class UC5 {

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submitting booking requests
        bookingQueue.addRequest(new Reservation("Pavithra", "Single"));
        bookingQueue.addRequest(new Reservation("Arun", "Suite"));
        bookingQueue.addRequest(new Reservation("Meena", "Double"));

        // Display queue (FIFO order)
        bookingQueue.viewRequests();

        // Show next request (without removing)
        System.out.println("\nNext request to process: " + bookingQueue.peekNext());
    }
}