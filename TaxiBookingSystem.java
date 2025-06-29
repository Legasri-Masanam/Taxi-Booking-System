import java.util.*;

public class TaxiBookingSystem {
    static List<Taxi> taxis = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int customerCounter = 1;

    public static void main(String[] args) {
        System.out.print("Enter number of taxis: ");
        int numTaxi = sc.nextInt();

        initializeTaxi(numTaxi);

        while (true) {
            System.out.println("\n1. Book Taxi\n2. Display Taxi Details\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    bookTaxi();
                    break;
                case 2:
                    displayTaxiDetails();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void initializeTaxi(int n) {
        for (int i = 1; i <= n; i++)
            taxis.add(new Taxi(i));
    }

    public static void bookTaxi() {
        int customerId = customerCounter++;

        System.out.print("\nEnter Pickup Point (A-F): ");
        char pickup = sc.next().toUpperCase().charAt(0);

        System.out.print("Enter Destination Point (A-F): ");
        char destination = sc.next().toUpperCase().charAt(0);

        System.out.print("Enter Pickup Time (1 to 12): ");
        int pickupTime = sc.nextInt();

        Taxi selectedTaxi = null;
        int minDistance = Integer.MAX_VALUE;

        for (Taxi taxi : taxis) {
            if (taxi.isAvailable(pickupTime)) {
                int distance = Math.abs(taxi.currentPoint - pickup);

                if (distance < minDistance ||
                        (distance == minDistance && (selectedTaxi == null || taxi.totalEarnings < selectedTaxi.totalEarnings))) {
                    selectedTaxi = taxi;
                    minDistance = distance;
                }
            }
        }

        if (selectedTaxi == null) {
            System.out.println("Booking rejected. Taxi not available.");
            return;
        }

        int dropTime = pickupTime + Math.abs(destination - pickup);
        int amount = selectedTaxi.calculateEarnings(pickup, destination);
        int bookingId = selectedTaxi.bookings.size() + 1;

        Booking booking = new Booking(bookingId, customerId, pickup, destination, pickupTime, dropTime, amount);
        selectedTaxi.addBooking(booking);

        System.out.println("Taxi-" + selectedTaxi.id + " booked successfully. Amount: Rs." + amount);
    }

    public static void displayTaxiDetails() {
        for (Taxi taxi : taxis) {
            System.out.println("\nTaxi-" + taxi.id + " Total Earnings: Rs." + taxi.totalEarnings);
            for (Booking booking : taxi.bookings) {
                System.out.println("BookingID: " + booking.bookingId + " | CustomerID: " + booking.customerId +
                        " | From: " + booking.from + " | To: " + booking.to +
                        " | PickupTime: " + booking.pickupTime + " | DropTime: " + booking.dropTime +
                        " | Amount: Rs." + booking.amount);
            }
        }
    }
}
