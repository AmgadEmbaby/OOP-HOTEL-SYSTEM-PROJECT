import javax.swing.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;

public class Guests {

    private String userName;
    private String passWord;
    private LocalDate dateOfBirth;
    private double Balance =0;
    private String address;
    private Gender gender;
    private static ArrayList<Reservations> guestReservations = new ArrayList<>();


    Scanner input = new Scanner(System.in);


    enum Gender {
        male, female;

    }

    public Guests(String userName, String address, double balance, LocalDate dateOfBirth, String passWord, Gender gender) {
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

    public void cancelBooking(int reservationID) {
        Reservations res = Database.findReservation(reservationID);

        if (res != null && (res.getStatus() == Reservations.ReservationStatus.PENDING ||
                res.getStatus() == Reservations.ReservationStatus.CONFIRMED)) {

            double fee = res.calculateCancellationFee();

            if (res.getMethod() == Invoices.PaymentMethod.ONLINE) {

                double refund = res.getTypeDesired().getPricePerNight() - fee;
                this.setBalance(this.getBalance() + refund);

                if (fee > 0) {
                    System.out.println("Late cancellation. 50% penalty kept. Refunded: $" + refund);
                } else {
                    System.out.println("Early cancellation. Full refund of $" + refund + " processed.");
                }
            }
            else {
              .
                if (fee > 0) {
                    this.setBalance(this.getBalance() - fee);
                    System.out.println("Late cancellation fee of $" + fee + " charged to your account.");
                } else {
                    System.out.println("Early cancellation. No fees applied.");
                }
            }

            res.setStatus(Reservations.ReservationStatus.CANCELLED);

            // If the receptionist had already assigned a room number, make it available
            if (res.getRoom() != null) {
                res.getRoom().setStatus(Rooms.RoomStatus.AVAILABLE);
            }

            System.out.println("Reservation " + reservationID + " is now CANCELLED.");

        } else {
            System.out.println("Error: Reservation not found or cannot be cancelled at this stage.");
        }
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


        Guests tempGuest = new Guests (tempUsername, tempAddress, tempBalance, DOB , tempPassword, tempGender);
                GuestValidation.validateRegister(tempGuest); // if valid, allows next

        this.userName = tempUsername;
        this.address = tempAddress;
        this.Balance = tempBalance;
        this.dateOfBirth = DOB;
        this.passWord = tempPassword;
        this.gender = tempGender;

        Database.getGuestList().add(this); // add guest to database
        System.out.println("Registration successful!");
    }

    public void showAvailableRooms(){

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


    public void makeReservation(Guests Guests, RoomType roomType, LocalDate checkIn, LocalDate checkOut ) throws Exception {
        try {
            Reservations current = new Reservations( Guests, roomType, checkIn,  checkOut );
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



}
