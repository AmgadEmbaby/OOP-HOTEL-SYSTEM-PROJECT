import java.time.LocalDate;
import java.util.*;

public class Guests {

    private String userName;
    private String passWord;
    private LocalDate dateOfBirth;
    private double Balance =0;
    private String address;
    private Gender gender;
    private boolean loginStatues;
    private  ArrayList<Reservations> guestReservations = new ArrayList<>();
    private  ArrayList<Invoices> guestInvoices  = new ArrayList<>();


    Scanner input = new Scanner(System.in);


    enum Gender {
        male, female;

    }

    public Guests(String userName, String address,  LocalDate dateOfBirth, String passWord, Gender gender) {
        this.userName = userName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.passWord = passWord;
        this.gender = gender;
    }

    public Guests() {
    }


    public String getUserName() {
        return userName;
    }

    public boolean isLoginStatues() {
        return loginStatues;
    }

    public void setLoginStatues(boolean loginStatues) {
        this.loginStatues = loginStatues;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public  ArrayList<Reservations> getGuestReservations() {
        return guestReservations;
    }

    public ArrayList<Invoices> getGuestInvoices() {
        return guestInvoices;
    }

    public void setGuestInvoices(ArrayList<Invoices> guestInvoices) {
        this.guestInvoices = guestInvoices;
    }
    public void addNewInvoiceForTheGuestList(Invoices invoices){
        this.getGuestInvoices().add(invoices);
    }


    public void ShowOnGoingInvoices(){
        for(Invoices invoices: this.getGuestInvoices()){
            if(invoices.getStatus()== Invoices.InvoiceStatus.UNPAID){
                System.out.println("The Invoice with ID:"+invoices.getInvoiceId()+" is still unpaid");

            }
        }
    }


    public void onlinePaymentForTheOngoingInvoices(String invoiceID){
        for(Invoices invoices:this.getGuestInvoices()){
            if(invoices.getInvoiceId().equalsIgnoreCase(invoiceID)){
                if(invoices.getStatus()== Invoices.InvoiceStatus.UNPAID && invoices.getPaymentmethod() == Invoices.PaymentMethod.ONLINE && this.getBalance() > invoices.getTotalamount() ){
                    System.out.println("Processing online payment...");
                    System.out.println("Sufficient balance in account ");
                    this.setBalance(this.getBalance()-invoices.getTotalamount());
                    System.out.println("payment Successful");

                }

            }
        }
    }

    public void AddTobalance(int value){
        this.setBalance(this.getBalance()+ value);
    }




    public void cancelBooking(int reservationID) {
        Reservations res = Database.findReservation(reservationID);

        if (res == null || res.getStatus() == Reservations.ReservationStatus.COMPLETED ||
                res.getStatus() == Reservations.ReservationStatus.CANCELLED) {
            System.out.println("Error: Reservation cannot be cancelled.");
            return;
        }

        double fee = res.calculateCancellationFee();

        if (res.getMethod() == Invoices.PaymentMethod.ONLINE) {
            double refund = res.getTypeDesired().getPricePerNight() - fee;
            this.setBalance(this.getBalance() + refund);
        } else if (fee > 0) {
            this.setBalance(this.getBalance() - fee);
        }

        res.cancelReservation();
        System.out.println("Reservation " + reservationID + " cancelled. Balance updated.");
    }

    public static Boolean login(String username, String passWord){
        boolean loginStatues = false;
        boolean usernameFound = false;
        boolean passwordFound= false;

        for(Guests guests:Database.getGuestList()){

            if(guests.getUserName().equalsIgnoreCase(username)){
                usernameFound = true;
                if(guests.getPassWord().equalsIgnoreCase(passWord)){
                    passwordFound = true;
                    System.out.println("Password found");
                    loginStatues = true;
                    return loginStatues;
                }

            }
        }
            if(!usernameFound){
                System.out.println("Username not found in the system plz re eneter  ");
            }
            else if(!passwordFound){
                System.out.println("Password entered is incorrect ");
            }
            return loginStatues;
    }



    public void Register() {
        Guests.Gender tempGender = null;
        System.out.println("Welcome to the our hotels app!!! ");
        System.out.print("Please enter your desired username: ");
        String tempUsername = input.nextLine();
        System.out.print("Enter your gender: ");
        String InputTempGender = input.nextLine();
        if (InputTempGender.equalsIgnoreCase("male")) {
            tempGender = Guests.Gender.male;
        } else {
            tempGender = Guests.Gender.female;
        }
        // VALIDATION FOR INPUT HERE TO BE USED LATER
        System.out.print("Enter your date of birth(yyyy/MM/DD), pressing \" Enter\" after each segment: ");
        int tempYear = input.nextInt();
        int tempMonth = input.nextInt();
        int tempDay = input.nextInt();
        LocalDate DOB = LocalDate.of(tempYear, tempMonth, tempDay);
        input.nextLine();
        System.out.print("Enter your password: ");
        String tempPassword = input.nextLine();
        System.out.print("Enter your address: ");
        String tempAddress = input.nextLine();
        System.out.print("Enter your balance: ");
        int tempBalance = input.nextInt();
        this.userName = tempUsername;
        this.address = tempAddress;
        this.Balance = tempBalance;
        this.dateOfBirth = DOB;
        this.passWord = tempPassword;
        this.gender = tempGender;
        Database.addGuests(this);
    }

    public void showAvilableRooms(){

    }

    public void displayGuestInfo(){
        System.out.println("--- GUESTS DETAILS ---");
        System.out.println("User Name: " + userName);
        System.out.println("Birth Date: " + dateOfBirth);
        System.out.println("Balance: " + Balance);
        System.out.println("Address: " + address);
        System.out.println("Gender: " + gender);
        System.out.println("---------------------------");

    }


    public void makeReservation(Guests Guests, RoomType roomType, LocalDate checkIn, LocalDate checkOut,Invoices.PaymentMethod method ) throws Exception {
        try {
            Reservations current = new Reservations( Guests, roomType, checkIn,  checkOut,method );
            double finalPrice = current.getStayPrice();
            Invoices.InvoiceStatus initialStatus;
            if (method == Invoices.PaymentMethod.ONLINE) {
                if(this.getBalance()> finalPrice){
                    this.setBalance(this.getBalance()-finalPrice);
                    initialStatus = Invoices.InvoiceStatus.PAID;
                }
                else{
                    System.out.print("Insufficient balance plz recharge your balance then try again");
                    return;
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
            System.out.println("Your reservation ID is "+ current.getReservationID());
            Database.getReservationsList().add(current);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<RoomType, Integer> ViewAvilableRooms( LocalDate CheckIn, LocalDate Checkout){
        List<RoomType> AvliableRoomtype= Database.getAvailableRoomTypesList();
        Map<RoomType, Integer> availabilityResults = new HashMap<>();
        Boolean Avilable = true;
        int minNo ;


        for(RoomType type : AvliableRoomtype) {
            LocalDate tempDate = CheckIn;
            String roomTypeName;
            roomTypeName = type.getTypeName();
            Avilable = true;
            minNo = Database.getAvailableRoomCount(roomTypeName, tempDate);

            while (tempDate.isBefore(Checkout) && Avilable==true) {

                int RoomsAvilable= Database.getAvailableRoomCount(roomTypeName,tempDate);
                minNo = Math.min(minNo,RoomsAvilable);
                if(RoomsAvilable> 0){

                   tempDate= tempDate.plusDays(1);

                }else{
                    System.out.println("No " + type.getTypeName()+ " Rooms are available");
                    Avilable = false;
                }


            }
            if(Avilable== true){

                availabilityResults.put(type, minNo);

            }

        }
            return availabilityResults;




    }

    public Boolean  cancelReservation(int reservationId){


        for(Reservations r : this.getGuestReservations()) {

            if(r.getReservationID() == reservationId){

                if(r.getStatus()==Reservations.ReservationStatus.CANCELLED){
                    System.out.println("Failed, the Room with reservation ID "+ reservationId + " is already cancelled");
                    return false;
                }
                else{
                    r.cancelReservation();
                    System.out.println("Successful,the reservation with reservation ID "+ r.getReservationID()+ " has been cancelled ");
                    return true;
                }

            }

        }
        System.out.println("Failed, The room with ID"+ reservationId+ " is not in your account!" );
        return false;

    }


    public void extendReservation(int reservationID, int extraDays) {
        Reservations res = Database.findReservation(reservationID);

        if (res == null || res.getStatus() == Reservations.ReservationStatus.CANCELLED) {
            System.out.println("You cannot extend a non-existent or cancelled reservation.");
            return;
        }

        LocalDate currentCheckout = res.getCheckout();
        LocalDate newCheckout = currentCheckout.plusDays(extraDays);
        String typeNeeded = res.getTypeDesired().getTypeName();
        Rooms currentRoom = res.getRoom();

        // 1. Try to keep the same room
        if (Database.isRoomAvailableForDates(currentRoom, currentCheckout, newCheckout)) {
            res.processExtension(extraDays, currentRoom, newCheckout);
            return; //exit the function if current room is found
        }

        // 2. If same room isn't available, search for others
        System.out.println("Your current room is booked. Searching for other " + typeNeeded + " rooms...");
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
            System.out.println("Room " + alternativeRoom.getRoomNumber() + " is available! Moving you there.");
            res.processExtension(extraDays, alternativeRoom, newCheckout);
        } else {
            System.out.println("Sorry, no rooms of type " + typeNeeded + " are available for extension.");
        }
    }

//THIS METHOD acts as "virtual checkout" or a precheckout before physically checking out at the receptionist desk
    public void requestCheckout(int reservationID, Invoices.PaymentMethod preferredPayment) {

        Reservations res = Database.findReservation(reservationID);

        if (res == null || res.getStatus() != Reservations.ReservationStatus.CONFIRMED) {
            System.out.println("No active stay found for this ID.");
            return;
        }

        if (res.getRoom() != null) {
            res.getRoom().CalculateTotalAmenityCost();
        }


        if (res.getRoom().getTotalAmenityCost() > 0) {

            Receptionist.processRoomServicePayment(res.getRoom(), res, preferredPayment);


            res.getRoom().getAmenities().clear();
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
                // If online fails, we don't finish checkout!
                System.out.println("FAILED: Insufficient balance. Please pay at the front desk.");
                return;
            }
        }
        else if (amountToPayNow > 0) {
            System.out.println("PENDING: Guest must pay $" + amountToPayNow + " in person via " + preferredPayment);
            return;

        }

        res.setCheckedIn(false);
        System.out.println(" Checkout request Successful. Please drop your key at the Reception Desk.");




        double finalDebt = Database.calculateReservationTotal(reservationID);

        if (finalDebt <= 0) {

            res.setCheckedIn(false);
            System.out.println("Checkout successful. Room " + res.getRoom().getRoomNumber() + " is now available.");
        } else {

            System.out.println("CHECKOUT BLOCKED: Guest still owes $" + finalDebt + ". Payment required.");
        }



    }






    public void orderAmenity(int reservationID, String amenityName) {

        Reservations res = Database.findReservation(reservationID);

        Amenity item = Database.findAmenity(amenityName);

        if (res != null && item != null) {
            if (res.getRoom() != null) {
                res.getRoom().getAmenities().add(item);
                System.out.println("Success: " + amenityName + " added to Reservation #" + reservationID);
            } else {
                System.out.println("Error: This reservation doesn't have a room assigned yet.");
            }
        } else {
            System.out.println("Error: Reservation or Amenity not found.");
        }
    }











}
