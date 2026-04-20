import java.util.*;
import java.time.LocalDate;

public class GuestValidation {
    public static void validateRegister(Guests guest) {

        Validator.checkNotNull(guest, "Guest cannot be null.");
        UserValidation.validateUsername(guest.getUserName());
        UserValidation.validatePassword(guest.getPassWord());
        UserValidation.validateAddress(guest.getAddress());
        UserValidation.validateDOB(guest.getDateOfBirth());

        Validator.checkNumNotNegative(guest.getBalance(), "Balance");

        if (guest.getGender()== null) {
            throw new IllegalArgumentException("Gender must be specified");
        }


        //no duplicate usernames
        // g is existing guest
        //guest is new guest, trying to register
        for (Guests g : Database.getGuestList()){
            String username = guest.getUserName().trim();
            if (username.equalsIgnoreCase(g.getUserName())){
                throw new IllegalArgumentException("Username already exists.");
            }
        }

    }



    public static Guests validateGuestLogin(String userName, String passWord){
        Validator.checkStringNotEmpty(userName, "Username");
        Validator.checkStringNotEmpty(passWord, "Password");

        userName = userName.trim();
        for (Guests g : Database.getGuestList()){
            if (userName.equalsIgnoreCase(g.getUserName()) && passWord.equals(g.getPassWord())){
                return g;
            }
        }

        throw new IllegalArgumentException("Invalid username or password.");

    }


}
