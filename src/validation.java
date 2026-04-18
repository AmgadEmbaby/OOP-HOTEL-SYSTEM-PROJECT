
import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;

public class Validation {

       // ------------------------General----------------------------------------------------------------------------

        static String getValidUsername(ArrayList<Guests> guestList){

            Scanner input = new Scanner(System.in);
            String userName;

            while(true) {
                System.out.print("Enter username: ");
                userName = input.nextLine();

                //not empty
                if (userName == null || userName.trim().isEmpty()) {
                    System.out.print("Invalid: empty username");
                    continue;
                }

                //alphanumeric
                boolean validAlphanumeric = true;
                for (int i = 0; i < userName.length(); i++) {
                    char c = userName.charAt(i);
                    if (!Character.isLetterOrDigit(c)) {
                        validAlphanumeric = false;
                        break; // so it doesnt ignore one wrong character only
                    }
                }

                if(!validAlphanumeric){
                    System.out.println("Invalid: only letters and numbers allowed");
                    continue;
                }


                //no duplicates
                boolean validNotDuplicate = true;
                for (Guests guest : guestList) {
                    if (userName.equals(guest.getUserName())) { //if found in Guests
                        validNotDuplicate = false;
                        break;
                    }
                }
                if (!validNotDuplicate){
                    System.out.println("Invalid: username already exists");
                    continue;
                }

                return userName;
            }
        }




        public static String getValidPassword(){

            //mot empty, no less than 8

            Scanner input = new Scanner(System.in);
            String passWord;

            while (true) {
                System.out.print("Enter password: ");
                passWord = input.nextLine();

                if (passWord == null || passWord.length() < 8) {
                    System.out.println("Invalid password, minimum 8 characters");
                    continue;
                }
                return passWord;
            }
        }




   public static LocalDate getValidBirthdate() {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Enter year: ");
            int year = input.nextInt();

            System.out.print("Enter month: ");
            int month = input.nextInt();

            System.out.print("Enter day: ");
            int day = input.nextInt();

            try {
                LocalDate dateOfBirth = LocalDate.of(year, month, day);

                if (dateOfBirth.isAfter(LocalDate.now())) {
                    System.out.println("BirthDate you selected is in future. Please try again.");
                    continue;
                }

                return dateOfBirth;
            } catch(Exception datenonexistent){
                System.out.println("Invalid date. Try again.");
            }
        }
    }




     public static Guests.Gender getValidGender(){
               // return gender!=null; // if gender = null returns false
         Scanner input = new Scanner(System.in);

         while (true){
             System.out.print("Enter gender (male / female) ");
             String genderString = input.nextLine().toLowerCase();

            try {
                return Guests.Gender.valueOf(genderString);  // enum.valueOf() , throws excseption
            } catch(IllegalArgumentException invalidEnum){
                System.out.println("Invalid gender, please try again.");
             }
         }
    }




    public static String getValidAddress (){
            Scanner input = new Scanner(System.in);
            String address;

            while(true){
                System.out.print("enter address: ");
                address = input.nextLine();

                if(address == null || address.trim().isEmpty() ){
                    System.out.println("\"Invalid address. Please enter valid address.\"");
                    continue;
                }

                return address;
            }
    }



    public static double getValidBalance (){
           Scanner input = new Scanner(System.in);
           double balance;

           while(true){
               System.out.print("Enter balance: ");

               if (!input.hasNextDouble()) { //check for letter prevent crash
                   System.out.println("Invalid input. Enter a number.");
                   input.next(); // clearing prev input
                   continue;
               }


               balance = input.nextDouble();

               if(balance <= 0 ){
                   System.out.println("Balance is too low. Please enter valid balance.");
                   continue;
               }

               return balance;

           }

    }


public static int getValidWorkingHours(){
        Scanner input = new Scanner(System.in);
        int workingHours;

        while(true){
            System.out.print("Enter working hours between 0 and 50: ");

            if (!input.hasNextInt()) { //check for letter to prevent crash
                System.out.println("Invalid input. Enter a number.");
                input.next(); // clearing previous input
                continue;
            }

            workingHours = input.nextInt();

            if(workingHours < 0 ){
                System.out.println("Working hours can't be negative. Please enter valid number.");
                continue;
            } else if (workingHours > 50) {
                System.out.println("Working hours exceed limit. Please enter valid number.");
                continue;
            }

            return workingHours;
        }

    }

    //Login Validation,
    // TODO:
    //  username exists in arr of users, pass matches



    //------------------------------- Room validation ---------------------------------------------------------------




    // validate Room type
    public static RoomType getValidRoomType() {

        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Enter room type (single / double / suite): ");
            String myType = input.nextLine().toLowerCase();
            if (myType.equals("single") ||
                    myType.equals("double") ||
                    myType.equals("suite")) {

                return Database.findRoomType(myType);
            }
            System.out.println("Invalid room type, try again.");
        }
    }

    //TODO
    // only one room type per room
    // amenities in this room not empty


    //room price more than 0
    // variables msh 7a2ee2eya: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //roomPrice
    public static double getValidRoomPrice (){
        Scanner input = new Scanner(System.in);
        double roomPrice;

        while(true){
            System.out.print("Enter Price: ");

            if (!input.hasNextDouble()) { //check for letter prevent crash
                System.out.println("Invalid input. Enter a number.");
                input.next(); // clearing prev input
                continue;
            }


            roomPrice = input.nextDouble();

            if(roomPrice <= 0 ){
                System.out.println("Price is too low. Please enter valid Price.");
                continue;
            }

            return roomPrice;

        }

    }


    // --------------- Reservation ---------------------------------------------------------------------------------
    //TODO
    //logged in to reserve
    //room exists & not booked
    // balance >= price
    //Date not in the past + present to future

    //--------------------- while reserved -------------------------------------------------------------------------
    //TODO
    //prevent someone else booking

    //-------------------- cancel reservation -----------------------------------------------------------------------
    //TODO
    //user id corresponds reservation
    //reserv exists
    //not expired

    //invoice and payment, TBD!

    //------------------ ADMIN --------------------------------------------------------------------------------------
    //TODO
    //cant manage if not exist
    //cant delete if in use
    //receptionist limits


    //-------------- IN MEMORY VALIDATION ----------------------------------------------------------------------------
    //TODO
    //object valid b4 adding
    //no null
    //no duplicate guest or room


}
