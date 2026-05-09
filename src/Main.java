import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        boolean AppLoop = true;
        boolean Guestloop = false;
        boolean Guestlogged = false;
        String choice;
        int choiceInt;
        int terminate;

        while (AppLoop) {

            System.out.println("--- Hotel System Initialized ---");
            System.out.print("Press (1) to continue (0) to terminate: ");
            terminate = AuthMenu.numberScanner();

            while (!(terminate == 1 || terminate == 0)) {
                System.out.print("Invalid option, enter again:");
                terminate = AuthMenu.numberScanner();
            }

            if (terminate == 0) {
                AppLoop = false;
                break;
            }

            System.out.print("Please pick who is using this program: ");
            choice = input.nextLine();

            while (!(choice.equalsIgnoreCase("Guest") ||
                    choice.equalsIgnoreCase("Admin") ||
                    choice.equalsIgnoreCase("Receptionist"))) {
                System.out.print("INVALID, please re-enter: ");
                choice = input.nextLine();
            }

            if (choice.equalsIgnoreCase("Guest")) {

                Guests currentGuests = new Guests();
                System.out.print("Press (1) to login, press (2) to signup: ");
                choiceInt = AuthMenu.numberScanner();

                while (!(choiceInt == 1 || choiceInt == 2)) {
                    System.out.print("Invalid, Please re-enter:");
                    choiceInt = AuthMenu.numberScanner();
                }

                if (choiceInt == 1) {

                    currentGuests = null;
                    while (currentGuests == null) {
                        try {
                            System.out.print("Enter your username: ");
                            String username = input.nextLine();
                            System.out.print("Enter your Password: ");
                            String password = input.nextLine();
                            currentGuests = GuestValidation.validateGuestLogin(username, password);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Login failed: " + e.getMessage());
                        }
                    }

                    System.out.println("Logged in successfully");
                    Guestloop = true;
                    Guestlogged = true;
                    currentGuests.displayGuestInfo();

                    while (Guestloop) {
                        System.out.println("Enter the number of what you want to do next");
                        System.out.println("1 - View available rooms");
                        System.out.println("2 - Make Reservations");
                        System.out.println("3 - View Reservations");
                        System.out.println("4 - Cancel Reservation");
                        System.out.println("5 - Pay Invoice");
                        System.out.println("6 - Notify receptionist for checkout");
                        System.out.println("7 - Recharge balance");
                        System.out.println("8 - View ongoing invoices");
                        System.out.println("0 - Logout");
                        choiceInt = AuthMenu.numberScanner();

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
                                        System.out.print("Enter check-in date DD MM YYYY: ");
                                        tempDateCheckIn = LocalDate.of(
                                                input.nextInt(), input.nextInt(), input.nextInt());
                                        input.nextLine();
                                        System.out.print("Enter check-out date DD MM YYYY: ");
                                        tempDateOut = LocalDate.of(
                                                input.nextInt(), input.nextInt(), input.nextInt());
                                        input.nextLine();
                                        ReservationsValidation.validateReservation(
                                                currentGuests, tempDateCheckIn, tempDateOut);
                                        break;
                                    } catch (IllegalArgumentException e) {
                                        System.out.println(e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Invalid input. Try again.");
                                        input.nextLine();
                                    }
                                }
                                System.out.println(Guests.ViewAvilableRooms(tempDateCheckIn, tempDateOut));
                                break;

                            case 2:
                                LocalDate checkIn = null;
                                LocalDate checkOut = null;
                                while (true) {
                                    try {
                                        System.out.print("Enter check-in date DD MM YYYY: ");
                                        checkIn = LocalDate.of(
                                                input.nextInt(), input.nextInt(), input.nextInt());
                                        input.nextLine();
                                        System.out.print("Enter check-out date DD MM YYYY: ");
                                        checkOut = LocalDate.of(
                                                input.nextInt(), input.nextInt(), input.nextInt());
                                        input.nextLine();
                                        ReservationsValidation.validateReservation(
                                                currentGuests, checkIn, checkOut);
                                        break;
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Error: " + e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Invalid input. Try again.");
                                        input.nextLine();
                                    }
                                }

                                System.out.println("Available room types: " +
                                        Database.getAvailableRoomTypesList());
                                RoomType tempRoomType = null;
                                String temp = input.nextLine();
                                boolean roomFound = false;
                                while (!roomFound) {
                                    try {
                                        tempRoomType = RoomValidation.validateRoomTypeName(temp);
                                        roomFound = true;
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid room type. Please re-enter:");
                                        temp = input.nextLine();
                                    }
                                }

                                Invoices.PaymentMethod finalmethod = null;
                                System.out.println("Payment method (Cash, Online, Credit):");
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
                                        System.out.println("Invalid. Re-enter:");
                                        tempMethod = input.nextLine();
                                    }
                                }

                                try {
                                    currentGuests.makeReservation(currentGuests, tempRoomType,
                                            checkIn, checkOut, finalmethod);
                                    System.out.println("Reservation successful!");
                                } catch (Exception e) {
                                    System.out.println("Something went wrong: " + e.getMessage());
                                }
                                break;

                            case 3:
                                System.out.println(currentGuests.getGuestReservations());
                                break;

                            case 4:
                                System.out.print("Enter reservation ID to cancel: ");
                                int tempID = input.nextInt();
                                input.nextLine();
                                currentGuests.cancelReservation(tempID);
                                break;

                            case 5:
                                System.out.println("Enter invoice ID to pay:");
                                String tempInvoiceId = input.nextLine();
                                currentGuests.onlinePaymentForTheOngoingInvoices(tempInvoiceId);
                                break;

                            case 6:
                                System.out.println("Enter reservation ID to check out from:");
                                tempID = input.nextInt();
                                input.nextLine();
                                System.out.println("Payment method (Cash, Online, Credit):");
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
                                        System.out.println("Invalid. Re-enter:");
                                        finalmethod = null;
                                        tempMethod = input.nextLine();
                                    }
                                }
                                currentGuests.requestCheckout(tempID, finalmethod);
                                break;

                            case 7:
                                System.out.println("Enter amount to add to balance:");
                                int tempinc = input.nextInt();
                                input.nextLine();
                                while (tempinc < 0) {
                                    System.out.println("Cannot be negative. Re-enter:");
                                    tempinc = input.nextInt();
                                }
                                currentGuests.AddTobalance(tempinc);
                                break;

                            case 8:
                                System.out.println(currentGuests.getGuestInvoices());
                                break;

                            default:
                                System.out.println("Invalid option.");
                        }
                    }

                } else if (choiceInt == 2) {
                    currentGuests.Register();
                }

            } else if (choice.equalsIgnoreCase("Receptionist")) {
                System.out.println("Receptionist menu coming soon.");
            }
        }
    }
}