import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hotel System Initialized!");

        Scanner input = new Scanner(System.in);
        Guests test = new Guests();
        Guests test2 = new Guests();
        Guests test3 = new Guests();
        Rooms testRoom = Database.getRoomList().get(0);
        Rooms testRoom2 = Database.getRoomList().get(1);
        LocalDate CheckIn = LocalDate.of(2026, 4, 21);
        LocalDate CheckOut = LocalDate.of(2026, 4, 25);

        LocalDate CheckIn2 = LocalDate.of(2026, 6, 21);
        LocalDate CheckOut2 = LocalDate.of(2026, 6, 25);

        try {
            test.makeReservation(test2,testRoom,CheckIn,CheckOut);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            test.makeReservation(test3,testRoom2,CheckIn2,CheckOut2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(test.ViewAvilableRooms(CheckIn,CheckOut));






    }
}

