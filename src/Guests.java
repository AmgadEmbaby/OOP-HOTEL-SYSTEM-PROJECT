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
    private static ArrayList<Reservations> guestReservations = new ArrayList<>();


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



}
