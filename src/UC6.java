import java.util.*;

// Reservation (from UC5)
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
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// Booking Request Queue (UC5)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (UC6)
class BookingService {

    private InventoryService inventory;

    // Track allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → assigned room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    private int roomCounter = 1; // for unique ID generation

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {

        String type = r.getRoomType();

        // Check availability
        if (inventory.getAvailable(type) <= 0) {
            System.out.println("No rooms available for " + type +
                    " (Guest: " + r.getGuestName() + ")");
            return;
        }

        // Generate unique room ID
        String roomId;
        do {
            roomId = type.substring(0, 1).toUpperCase() + roomCounter++;
        } while (allocatedRoomIds.contains(roomId));

        // Store in set (prevent duplicates)
        allocatedRoomIds.add(roomId);

        // Map room type → room IDs
        roomAllocations.putIfAbsent(type, new HashSet<>());
        roomAllocations.get(type).add(roomId);

        // Update inventory (IMPORTANT)
        inventory.decrement(type);

        // Confirm booking
        System.out.println("Booking Confirmed!");
        System.out.println("Guest: " + r.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Room ID: " + roomId);
        System.out.println("------------------------");
    }
}

// Main
public class UC6 {

    public static void main(String[] args) {

        // Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);

        // Setup Queue (UC5)
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Pavithra", "Single"));
        queue.addRequest(new Reservation("Arun", "Single"));
        queue.addRequest(new Reservation("Meena", "Single")); // should fail

        // Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Process queue (FIFO)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processBooking(r);
        }
    }
}