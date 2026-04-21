import java.time.LocalDate;

public class UserValidation {


    public static void validateUsername(String userName) {
        Validator.checkStringNotEmpty(userName, "Username");

        //alphanumeric
        for (int i = 0; i < userName.length(); i++) {
            char c = userName.charAt(i);
            if (! Character.isLetterOrDigit(c)) {
                throw new IllegalArgumentException("Username must contain letters or digits only.");
            }
        }
    }


    public static void validatePassword(String passWord) {
        Validator.checkStringNotEmpty(passWord, "Password");

        if (passWord.length() <8){
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

    }

    public static void validateDOB(LocalDate DOB){
        Validator.checkNotNull(DOB, "Date of birth is required");

        if (DOB.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }

        if(DOB.plusYears(18).isAfter(LocalDate.now())){
         throw new IllegalArgumentException("User must be at least 18 years old.");
         }
    }


    public static void validateAddress(String address) {
        Validator.checkStringNotEmpty(address, "Address");
    }

    //gender validation in specific class, staff gender, guest gender



}
