import javax.xml.crypto.Data;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Map; // Added missing import

public class Main {
    public static void main(String[] args) throws Exception {
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
                        while(currentGuests == null) try {
                            System.out.print("Enter your username:");
                            String username = input.nextLine();
                            System.out.print("Enter your Password: ");
                            String password = input.nextLine();
                            // currentGuests1=Guests.login(username, password);
                            currentGuests = GuestValidation.validateGuestLogin(username, password);
                        } catch (IllegalArgumentException e){
                            System.out.println("Login failed: " + e.getMessage());
                        }
                        System.out.println("Logged in successfully");
                        Guestloop= true ;
                        Guestlogged = true;
                        currentGuests.displayGuestInfo();

                        while(Guestloop){
                            System.out.println("Enter the number of what you want to do next");
                            System.out.println("1-View available rooms");
                            System.out.println("2-Make Reservations");
                            System.out.println("3-View Reservations");
                            System.out.println("4-Cancel Reservation");
                            System.out.println("5-Pay Invoice");
                            System.out.println("6-Notify the receptionist for checking out");
                            System.out.println("7-Recharge balance");
                            System.out.println("8-View ongoing invoices");
                            System.out.println("0- to logout and return to the previous menu");
                            choiceInt = AuthMenu.numberScanner();
                            while(choiceInt<0&&choiceInt>9){
                                System.out.println("Invalid option please re eneter");
                                choiceInt = AuthMenu.numberScanner();
                                input.nextLine();
                            }
                            switch (choiceInt) {
                                case 0:
                                    Guestloop = false;
                                    Guestlogged = false;
                                    break;
                                case 1:
                                    LocalDate tempDateCheckIn = null;
                                    LocalDate tempDateOut = null;

                                    while (true) {

                                        try {

                                            System.out.print("Dear guest please enter the check in date DD/MM/YYYY: ");
                                            int tempDay = input.nextInt();
                                            int tempMonth = input.nextInt();
                                            int tempYear = input.nextInt();
                                            input.nextLine();
                                            //LocalDate tempDateCheckIn;
                                            tempDateCheckIn = LocalDate.of(tempYear, tempMonth, tempDay);
                                            System.out.print("Dear guest please enter the check in date DD/MM/YYYY: ");
                                            int tempDay2 = input.nextInt();
                                            int tempMonth2 = input.nextInt();
                                            int tempYear2 = input.nextInt();
                                            input.nextLine();
                                            //LocalDate tempDateOut;
                                            tempDateOut = LocalDate.of(tempYear2, tempMonth2, tempDay2);

                                            ReservationsValidation.validateReservation(currentGuests, tempDateCheckIn, tempDateOut);
                                            break;

                                        } catch (IllegalArgumentException e) {
                                            System.out.println(e.getMessage());
                                        } catch (Exception e) {
                                            System.out.println("Invalid input. Try again.");
                                            input.nextLine();
                                        }

                                    System.out.println(Guests.ViewAvilableRooms(tempDateCheckIn, tempDateOut));
                                    break;
                            }
                                case 2:
                                    System.out.println("Processing reservation...");
                                    LocalDate checkIn = null;
                                    LocalDate checkOut = null;


                                    while (true) {
                                        try {
                                            System.out.print("Enter check-in date (DD MM YYYY): ");
                                            int d1 = input.nextInt();
                                            int m1 = input.nextInt();
                                            int y1 = input.nextInt();
                                            input.nextLine();
                                            checkIn = LocalDate.of(y1, m1, d1);

                                            System.out.print("Enter check-out date (DD MM YYYY): ");
                                            int d2 = input.nextInt();
                                            int m2 = input.nextInt();
                                            int y2 = input.nextInt();
                                            input.nextLine();
                                            checkOut = LocalDate.of(y2, m2, d2);

                                            ReservationsValidation.validateReservation(currentGuests, checkIn, checkOut);
                                            break;

                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Error: " + e.getMessage());
                                        } catch (Exception e) {
                                            System.out.println("Invalid input format. Please try again.");
                                            input.nextLine();
                                        }
                                    }


                                    System.out.println("Please enter the desired roomtype from the following roomtypes");
                                    System.out.println(Database.getAvailableRoomTypesList());
                                    RoomType tempRoomType = null;
                                    String temp = input.nextLine();
                                    boolean roomFound = false;

                                    while (!roomFound) {
                                        try {
//                                            for (RoomType r : Database.getAvailableRoomTypesList()) {
//                                                if (r.getTypeName().equalsIgnoreCase(temp)) {
                                            tempRoomType = RoomValidation.validateRoomTypeName(temp); //logic alr in validation methoid
                                            roomFound = true;
                                            System.out.println("Room type found!");
//                                                    tempRoomType = r;
//                                                    break;
//                                                }
                                            }catch(IllegalArgumentException e){

                                            // If we finished checking all rooms and didn't find it, ask again
//                                            if (!roomFound) {
                                                System.out.println("Invalid Roomtype. Please re-enter:");
                                                temp = input.nextLine();
                                            }
                                        }


                                    Invoices.PaymentMethod finalmethod = null;
                                    System.out.println("Please enter the method of Payment (Cash, Online, Credit)");
                                    String tempMethod = input.nextLine();
                                    boolean validMethod = false;

                                    while (!validMethod) {
                                        if (tempMethod.equalsIgnoreCase("CASH")) {
                                            finalmethod = Invoices.PaymentMethod.CASH;
                                            validMethod = true;
                                        } else if (tempMethod.equalsIgnoreCase("ONLINE")) {
                                            finalmethod = Invoices.PaymentMethod.ONLINE;
                                            validMethod = true;
                                        } else if (tempMethod.equalsIgnoreCase("CREDIT")) {
                                            finalmethod = Invoices.PaymentMethod.CREDIT_CARD;
                                            validMethod = true;
                                        } else {
                                            System.out.println("Invalid method entered. Please re-enter:");
                                            tempMethod = input.nextLine();
                                        }
                                    }

                                    try {
                                        currentGuests.makeReservation(currentGuests, tempRoomType, checkIn, checkOut, finalmethod);
                                        System.out.println("Reservation successful!");
                                    } catch (Exception e) {

                                        System.out.println("Something went wrong: " + e.getMessage());
                                    }
                                    break;

                                case 3:
                                    System.out.println(currentGuests.getGuestReservations());
                                    break;
                                case 4:
                                    System.out.print("Please enter the ID of the reservation that will get cancelled: ");
                                    int tempID = input.nextInt();
                                    input.nextLine();
                                    currentGuests.cancelReservation(tempID);
                                    break;
                                case 5:
                                    System.out.println("Please enter the ID of the invoice that you would like to pay");
                                    String tempInvoiceId = input.nextLine();
                                    currentGuests.onlinePaymentForTheOngoingInvoices(tempInvoiceId);
                                    break;
                                case 6:

                                    System.out.println("Please enter the id of the reservation you are checking out from");
                                    tempID = input.nextInt();
                                    input.nextLine();
                                    System.out.println("Please enter the method of Payment (Cash, Online, Credit)");
                                    tempMethod = input.nextLine();
                                    validMethod = false;
                                    finalmethod = null;
                                    while (!validMethod) {
                                        if (tempMethod.equalsIgnoreCase("CASH")) {
                                            finalmethod = Invoices.PaymentMethod.CASH;
                                            validMethod = true;
                                        } else if (tempMethod.equalsIgnoreCase("ONLINE")) {
                                            finalmethod = Invoices.PaymentMethod.ONLINE;
                                            validMethod = true;
                                        } else if (tempMethod.equalsIgnoreCase("CREDIT")) {
                                            finalmethod = Invoices.PaymentMethod.CREDIT_CARD;
                                            validMethod = true;
                                        } else {
                                            System.out.println("Invalid method entered. Please re-enter:");
                                            finalmethod = null;
                                            tempMethod = input.nextLine();
                                        }
                                    }

                                    currentGuests.requestCheckout(tempID,finalmethod);
                                    break;
                                case 7:
                                    System.out.println("Enter the amount you want to incrase your balance with");
                                    int tempinc = input.nextInt();
                                    input.nextLine();
                                    while(tempinc <0){
                                        System.out.println("You cant enter a negative number plz reneter");
                                        tempinc = input.nextInt();
                                    }

                                    currentGuests.AddTobalance(tempinc);

                                case 8:
                                    System.out.println("Getting Guest's invoices...");
                                    System.out.println(currentGuests.getGuestInvoices());

                            }







                        }
                        
                    } else if (choiceInt==2) {
                        currentGuests.Register();

                    }


                } else if (choice.equalsIgnoreCase("Receptionist")) {
                    System.out.println("What do you want to do ");
                    boolean receptionistLoop = true;

                    
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
            Receptionist receptionist = new Receptionist("receptionist", "recep123",   LocalDate.of(1999,5,9), Staff.Role.RECEPTIONIST,  8);

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