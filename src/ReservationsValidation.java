import java.util.*;
import java.time.LocalDate;


public class ReservationsValidation {

    public static void validateReservation(
            Guests guest,
            Rooms room,
            LocalDate checkin,
            LocalDate checkout)throws Exception {

        if (guest == null){
            throw new IllegalArgumentException("Guest is required");
        }


//        if (! Guests.LOGGGEDIN() ){
//            throw new IllegalArgumentException("User must be logged in");
//        }

        if (room == null){
            throw new IllegalArgumentException("Room does not exist");
        }

        if (room.getStatus() != Rooms.RoomStatus.AVAILABLE){
            throw new IllegalArgumentException("Room is not currently available");
        }

        if (checkin == null || checkout == null){
            throw new IllegalArgumentException("Dates must be specified");
        }

        if (checkout.isBefore(checkin)){
            throw new IllegalArgumentException("Checkin date must be before Checkout date");
        }

        if (checkin.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Checkin Date cannot be in the past");
        }

        if (room.isBooked(checkin, checkout)){
            throw new IllegalArgumentException("Room already booked for selected dates.");
        }

        double totalPrice = 0 ; //= ??????? where
        if (guest.getBalance() < totalPrice){
            throw new IllegalArgumentException("Insufficient balance");
        }



    }

}
