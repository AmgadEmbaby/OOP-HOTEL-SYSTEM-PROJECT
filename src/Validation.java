import java.util.Set;
import java.time.LocalDate;

public class Validation {

    String[] existingUsers; // EDIT LAMA ARRAY YET3EMEL

    static boolean isValidUsername(String userName,String[] existingUsers){
// edit string name 7asab your string name
    //not empty
    if (userName == null || userName.trim().isEmpty() ){
        return false;
    }

    //alphanumeric
    for (int i=0; i < userName.length(); i++){
        char c = userName.charAt(i);
        if (!Character.isLetterOrDigit(c)) {
            return false;
        }
    }

    //no duplicates
    for (int i=0; i< existingUsers.length; i++){
        if (userName.equals(existingUsers[i])){ //if found in array
            return false;
        }
    }

    return true;
}


public static boolean isValidPassowrd(String passWord){

        //mot empty, no less than 8
    if (passWord == null || passWord.length() < 8){
        return false;
    }
    return true;
}

//public static boolean isValidBirthdate(int day, int month, int year){

      //  LocalDate dateOfBirth;

   // }










}
