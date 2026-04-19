import java.time.LocalDate;

public class StaffValidation {

    public static void validateStaffUser(Staff staff){
        Validator.checkNotNull(staff, "Staff member cannot be null.");
        UserValidation.validateUsername(staff.getUserName());
        UserValidation.validatePassword(staff.getPassWord());
        //UserValidation.validateAddress(staff.getAddress());
        UserValidation.validateDOB(staff.getDateOfBirth());

        if (staff.getWorkingHours() > 50){
            throw new IllegalArgumentException("Working Hours exceed limit (50 Hours).");
        }

        if (staff.getWorkingHours() <= 0){
            throw new IllegalArgumentException("Working Hours cannot be negative.");
        }


    }
}
