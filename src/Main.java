import java.time.LocalDate;
import java.util.Scanner;
import java.util.Map; // Added missing import

public class Main {
    public static void main(String[] args) {
        Guests guest1 = new Guests("Nour", "Cairo", LocalDate.of(2000, 5, 10), "pass123", Guests.Gender.female);
        Guests guest2 = new Guests("Halla", "Alexandria", LocalDate.of(1999, 3, 15), "pass456", Guests.Gender.female);
        Database.getGuestList().add(guest1);
        Database.getGuestList().add(guest2);


        Scanner input = new Scanner(System.in);
        boolean AppLoop = true;
        boolean Guestloop  = false;
        boolean Guestlogged = false;
        String choice;
        int choiceInt;
        int terminate;

        while(AppLoop == true){

            System.out.println("--- Hotel System Initialized ---");
            System.out.print("Press (1) to continue (0) to terminate: ");
            terminate = AuthMenu.numberScanner();
            if(!(terminate==1 || terminate==0)){
                while(!(terminate==1 || terminate==0)){
                    System.out.print("Invalid option, enter again:");
                    terminate = AuthMenu.numberScanner();

                }
            }
            if(terminate==0){
                AppLoop = false;
                break;
            }
            else{
                System.out.print("Please pick who is using this program: ");
                choice = input.nextLine();
                while (!(choice.equalsIgnoreCase("Guest") || choice.equalsIgnoreCase("Admin")||choice.equalsIgnoreCase("Receptionist"))){
                    System.out.print("INVALID, please renter");
                    choice = input.nextLine();
                }
                if(choice.equalsIgnoreCase("Guest")){
                    Guests currentGuests = new Guests();
                    System.out.print("Press (1) to login, press (2) to signup: ");
                    choiceInt = AuthMenu.numberScanner();
                    while(!(choiceInt== 1|| choiceInt==2)){
                        System.out.print("Invalid, Please renter:");
                        choiceInt = AuthMenu.numberScanner();
                    }
                    if(choiceInt==1){

                        boolean logiStatus = false;
                        currentGuests= null;
                        while(currentGuests == null) {
                            System.out.print("Enter your username:");
                            String username = input.nextLine();
                            System.out.print("Enter your Password: ");
                            String password = input.nextLine();
                             currentGuests=Guests.login(username, password);
                        }
                    }




            }








            }


        }












        LocalDate checkIn = LocalDate.of(2026, 4, 27);
        LocalDate checkOut = LocalDate.of(2026, 4, 29);

        try {
            // 3. Make a Reservation
            System.out.println("\n[Action] Guest 1 is booking a room...");

            // IMPORTANT: Ensure "Single" matches what is in your Database static block exactly
            RoomType desiredType = Database.getAvailableRoomTypesList().get(0);

            guest1.makeReservation(guest1, desiredType, checkIn, checkOut, Invoices.PaymentMethod.ONLINE);

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