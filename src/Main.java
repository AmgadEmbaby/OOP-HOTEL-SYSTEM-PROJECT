import java.time.LocalDate;
import java.util.Scanner;
import java.util.Map; // Added missing import

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Hotel System Initialized ---");

        // 1. Create Guests
        Guests guest1 = new Guests("Nour", "Cairo", LocalDate.of(2000, 5, 10), "pass123", Guests.Gender.female);
        Guests guest2 = new Guests("Halla", "Alexandria", LocalDate.of(1999, 3, 15), "pass456", Guests.Gender.female);

        Database.getGuestList().add(guest1);
        Database.getGuestList().add(guest2);

        LocalDate checkIn = LocalDate.of(2026, 4, 25);
        LocalDate checkOut = LocalDate.of(2026, 4, 28);

        try {
            // 3. Make a Reservation
            System.out.println("\n[Action] Guest 1 is booking a room...");

            // IMPORTANT: Ensure "Single" matches what is in your Database static block exactly
            RoomType desiredType = Database.getAvailableRoomTypesList().get(0);

            guest1.makeReservation(guest1, desiredType, checkIn, checkOut);

            System.out.println("\n[Check] Checking availability for the same period:");
            Map<RoomType, Integer> availability = guest1.ViewAvilableRooms(checkIn, checkOut);

            for (Map.Entry<RoomType, Integer> entry : availability.entrySet()) {
                System.out.println(entry.getKey().getTypeName() + ": " + entry.getValue());
            }

            System.out.println("\n[Action] Receptionist checking in the guest...");
            Receptionist receptionist = new Receptionist();

            if (!Database.getReservationsList().isEmpty()) {
                int resID = Database.getReservationsList().get(0).getReservationID();
                receptionist.checkIn(resID);
            }

            // 6. Final Status View
            System.out.println("\n--- Final System State ---");
            receptionist.viewRooms();
            receptionist.viewReservations();

        } catch (Exception e) {
            System.out.println("Error in logic: " + e.getMessage());
            e.printStackTrace();
        }
    }
}