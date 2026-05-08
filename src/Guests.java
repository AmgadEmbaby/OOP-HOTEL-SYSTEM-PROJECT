import java.time.LocalDate;
import java.util.*;

public class Guests {

    private String userName;
    private String passWord;
    private LocalDate dateOfBirth;
    private double Balance = 0;
    private String address;
    private Gender gender;
    private boolean loginStatues = false;
    private ArrayList<Reservations> guestReservations = new ArrayList<>();
    private ArrayList<Invoices> guestInvoices = new ArrayList<>();

    Scanner input = new Scanner(System.in);

    enum Gender {
        male, female;
    }

    public Guests(String userName, String address, LocalDate dateOfBirth, String passWord, Gender gender) {
        this.userName = userName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.passWord = passWord;
        this.gender = gender;
    }

    public Guests() {
    }

    public String getUserName() { return userName; }
    public boolean isLoginStatues() { return loginStatues; }
    public void setLoginStatues(boolean loginStatues) { this.loginStatues = loginStatues; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPassWord() { return passWord; }
    public void setPassWord(String passWord) { this.passWord = passWord; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public double getBalance() { return Balance; }
    public void setBalance(double balance) { Balance = balance; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public ArrayList<Reservations> getGuestReservations() { return guestReservations; }
    public ArrayList<Invoices> getGuestInvoices() { return guestInvoices; }
    public void setGuestInvoices(ArrayList<Invoices> guestInvoices) { this.guestInvoices = guestInvoices; }

    public void addNewInvoiceForTheGuestList(Invoices invoices) {
        this.getGuestInvoices().add(invoices);
    }

    public void ShowOnGoingInvoices() {
        for (Invoices invoices : this.getGuestInvoices()) {
            if (invoices.getStatus() == Invoices.InvoiceStatus.UNPAID) {
                System.out.println("The Invoice with ID:" + invoices.getInvoiceId() + " is still unpaid");
            }
        }
    }

    public void onlinePaymentForTheOngoingInvoices(String invoiceID) throws Exception {
        boolean invoiceFound = false;
        for (Invoices invoices : this.getGuestInvoices()) {
            if (invoices.getInvoiceId().equalsIgnoreCase(invoiceID)) {
                invoiceFound = true;
                if (invoices.getStatus() == Invoices.InvoiceStatus.UNPAID && invoices.getPaymentmethod() == Invoices.PaymentMethod.ONLINE) {
                    if (this.getBalance() >= invoices.getTotalamount()) {
                        this.setBalance(this.getBalance() - invoices.getTotalamount());
                        invoices.setStatus(Invoices.InvoiceStatus.PAID);
                    } else {
                        throw new Exception("Insufficient balance to pay invoice.");
                    }
                } else {
                    throw new Exception("Invoice is already paid or not eligible for online payment.");
                }
            }
        }
        if (!invoiceFound) throw new Exception("Invoice ID not found.");
    }

    public void AddTobalance(int value) {
        this.setBalance(this.getBalance() + value);
    }

    public void cancelBooking(int reservationID) throws Exception {
        Reservations res = Database.findReservation(reservationID);

        if (res == null || res.getStatus() == Reservations.ReservationStatus.COMPLETED ||
                res.getStatus() == Reservations.ReservationStatus.CANCELLED) {
            throw new Exception("Error: Reservation cannot be cancelled.");
        }

        double fee = res.calculateCancellationFee();

        if (res.getMethod() == Invoices.PaymentMethod.ONLINE) {
            double refund = res.getTypeDesired().getPricePerNight() - fee;
            this.setBalance(this.getBalance() + refund);
        } else if (fee > 0) {
            this.setBalance(this.getBalance() - fee);
        }

        res.cancelReservation();
    }

    public static Guests login(String username, String passWord) {
        for (Guests guests : Database.getGuestList()) {
            if (guests.getUserName().equalsIgnoreCase(username)) {
                if (guests.getPassWord().equals(passWord)) {
                    guests.setLoginStatues(true);
                    return guests;
                }
            }
        }
        return null;
    }

    public void Register() {
        Guests.Gender tempGender = null;
        String tempUsername = input.nextLine();
        String InputTempGender = input.nextLine();
        if (InputTempGender.equalsIgnoreCase("male")) {
            tempGender = Guests.Gender.male;
        } else {
            tempGender = Guests.Gender.female;
        }
        int tempYear = input.nextInt();
        int tempMonth = input.nextInt();
        int tempDay = input.nextInt();
        LocalDate DOB = LocalDate.of(tempYear, tempMonth, tempDay);
        input.nextLine();
        String tempPassword = input.nextLine();
        String tempAddress = input.nextLine();
        int tempBalance = input.nextInt();
        this.userName = tempUsername;
        this.address = tempAddress;
        this.Balance = tempBalance;
        this.dateOfBirth = DOB;
        this.passWord = tempPassword;
        this.gender = tempGender;
        Database.addGuests(this);
    }

    public void displayGuestInfo() {
        System.out.println("--- GUESTS DETAILS ---");
        System.out.println("User Name: " + userName);
        System.out.println("Birth Date: " + dateOfBirth);
        System.out.println("Balance: " + Balance);
        System.out.println("Address: " + address);
        System.out.println("Gender: " + gender);
        System.out.println("---------------------------");
    }

    public void makeReservation(Guests Guests, RoomType roomType, LocalDate checkIn, LocalDate checkOut, Invoices.PaymentMethod method) throws Exception {
        Reservations current = new Reservations(Guests, roomType, checkIn, checkOut, method);
        double finalPrice = current.getStayPrice();
        Invoices.InvoiceStatus initialStatus;

        if (method == Invoices.PaymentMethod.ONLINE) {
            if (this.getBalance() >= finalPrice) {
                this.setBalance(this.getBalance() - finalPrice);
                initialStatus = Invoices.InvoiceStatus.PAID;
            } else {
                throw new Exception("Insufficient balance. Please recharge your balance and try again.");
            }
        } else {
            initialStatus = Invoices.InvoiceStatus.UNPAID;
        }

        Invoices bookingInvoice = new Invoices(
                finalPrice,
                method,
                current,
                Invoices.InvoiceType.BOOKING,
                initialStatus
        );

        Database.getInvoicesList().add(bookingInvoice);
        Database.getReservationsList().add(current);
        this.guestReservations.add(current);
    }

    public static Map<RoomType, Integer> ViewAvilableRooms(LocalDate CheckIn, LocalDate Checkout) {
        List<RoomType> AvliableRoomtype = Database.getAvailableRoomTypesList();
        Map<RoomType, Integer> availabilityResults = new HashMap<>();
        Boolean Avilable;
        int minNo;

        for (RoomType type : AvliableRoomtype) {
            LocalDate tempDate = CheckIn;
            String roomTypeName = type.getTypeName();
            Avilable = true;
            minNo = Database.getAvailableRoomCount(roomTypeName, tempDate);

            while (tempDate.isBefore(Checkout) && Avilable) {
                int RoomsAvilable = Database.getAvailableRoomCount(roomTypeName, tempDate);
                minNo = Math.min(minNo, RoomsAvilable);
                if (RoomsAvilable > 0) {
                    tempDate = tempDate.plusDays(1);
                } else {
                    Avilable = false;
                }
            }
            if (Avilable) {
                availabilityResults.put(type, minNo);
            }
        }
        return availabilityResults;
    }

    public Boolean cancelReservation(int reservationId) {
        for (Reservations r : this.getGuestReservations()) {
            if (r.getReservationID() == reservationId) {
                if (r.getStatus() == Reservations.ReservationStatus.CANCELLED) {
                    return false;
                } else {
                    r.cancelReservation();
                    guestReservations.remove(r);
                    return true;
                }
            }
        }
        return false;
    }

    public void extendReservation(int reservationID, int extraDays) throws Exception {
        Reservations res = Database.findReservation(reservationID);

        if (res == null || res.getStatus() == Reservations.ReservationStatus.CANCELLED) {
            throw new Exception("You cannot extend a non-existent or cancelled reservation.");
        }

        LocalDate currentCheckout = res.getCheckout();
        LocalDate newCheckout = currentCheckout.plusDays(extraDays);
        String typeNeeded = res.getTypeDesired().getTypeName();
        Rooms currentRoom = res.getRoom();

        if (Database.isRoomAvailableForDates(currentRoom, currentCheckout, newCheckout)) {
            res.processExtension(extraDays, currentRoom, newCheckout);
            return;
        }

        Rooms alternativeRoom = null;
        for (Rooms r : Database.getRoomList()) {
            if (r.getRoomtype().getTypeName().equalsIgnoreCase(typeNeeded)) {
                if (Database.isRoomAvailableForDates(r, currentCheckout, newCheckout)) {
                    alternativeRoom = r;
                    break;
                }
            }
        }

        if (alternativeRoom != null) {
            res.processExtension(extraDays, alternativeRoom, newCheckout);
        } else {
            throw new Exception("Sorry, no rooms of type " + typeNeeded + " are available for extension.");
        }
    }

    public void requestCheckout(int reservationID, Invoices.PaymentMethod preferredPayment) throws Exception {
        Reservations res = Database.findReservation(reservationID);

        if (res == null || res.getStatus() != Reservations.ReservationStatus.CONFIRMED) {
            throw new Exception("No active stay found for this ID.");
        }

        if (res.getRoom() != null) {
            res.getRoom().CalculateTotalAmenityCost();
        }

        double amountToPayNow = Database.calculateReservationTotal(reservationID);

        if (preferredPayment == Invoices.PaymentMethod.ONLINE && amountToPayNow > 0) {
            if (this.getBalance() >= amountToPayNow) {
                for (Invoices inv : this.getGuestInvoices()) {
                    if (inv.getReservation().getReservationID() == reservationID &&
                            inv.getStatus() == Invoices.InvoiceStatus.UNPAID) {
                        this.onlinePaymentForTheOngoingInvoices(inv.getInvoiceId());
                    }
                }
            } else {
                throw new Exception("FAILED: Insufficient balance. Please pay at the front desk.");
            }
        } else if (amountToPayNow > 0) {
            throw new Exception("PENDING: Guest must pay $" + amountToPayNow + " in person via " + preferredPayment);
        }

        res.setCheckedIn(false);
        double finalDebt = Database.calculateReservationTotal(reservationID);

        if (finalDebt <= 0) {
            res.setCheckedIn(false);
            if(res.getRoom() != null) {
                res.getRoom().setStatus(Rooms.RoomStatus.AVAILABLE);
                res.getRoom().getAmenities().clear();
            }
            res.setStatus(Reservations.ReservationStatus.COMPLETED);
        } else {
            throw new Exception("CHECKOUT BLOCKED: Guest still owes $" + finalDebt + ". Payment required.");
        }
    }

    public void orderAmenity(int reservationID, String amenityName) throws Exception {
        Reservations res = Database.findReservation(reservationID);
        Amenity item = Database.findAmenity(amenityName);

        if (res != null && item != null) {
            if (res.getRoom() != null) {
                res.getRoom().getAmenities().add(item);
            } else {
                throw new Exception("Error: This reservation doesn't have a physical room assigned yet.");
            }
        } else {
            throw new Exception("Error: Reservation or Amenity not found.");
        }
    }
}