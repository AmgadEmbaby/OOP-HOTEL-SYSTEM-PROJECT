import java.util.*;
import java.time.LocalDate;

public class GuestValidation {
    public static void registerValidation(Guests guest) {

        Validator.checkNotNull(guest, "Guest cannot be null.");
        UserValidation.validateUsername(guest.getUserName());
        UserValidation.validatePassword(guest.getPassWord());
        UserValidation.validateAddress(guest.getAddress());
        UserValidation.validateDOB(guest.getDateOfBirth());

        if (guest.getGender()== null) {
            throw new IllegalArgumentException("Gender must be specified");
        }

        if (guest.getBalance() < 0){
            throw new IllegalArgumentException("Balance cannot be negative.");
        }


        //TODO no duplicates


    }
}
