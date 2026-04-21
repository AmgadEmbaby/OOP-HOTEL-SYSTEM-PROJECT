import javax.swing.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;

public class Guests {

    private String userName;
    private String passWord;
    private LocalDate dateOfBirth;
    private double Balance;
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
        Balance = balance;
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


    public void makeReservation(Guests Guests, Rooms room, LocalDate checkIn, LocalDate checkOut ) throws Exception {
        try {
            Reservations current = new Reservations( Guests,  room,  checkIn,  checkOut );
            System.out.println("Your reservation ID is "+ current.getReservationID());



          //  room.setStatus(Rooms.RoomStatus.OCCUPIED);
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
