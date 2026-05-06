import java.time.LocalDate;


public class ReservationsValidation {

    public static void validateReservation(
            Guests guest,
//            Rooms room,
            LocalDate checkin,
            LocalDate checkout
            )throws IllegalArgumentException {

        if (guest == null){
            throw new IllegalArgumentException("Guest is required");
        }

//TODO

//        if (! Guests.LOGGGEDIN() ){
//            throw new IllegalArgumentException("User must be logged in");
//        }


//------------------------------------------moved---------------------------------------------
//        if (room == null){
//            throw new IllegalArgumentException("Room does not exist");
//        }

//        if (room.getStatus() != Rooms.RoomStatus.AVAILABLE){
//            throw new IllegalArgumentException("Room is not currently available");
//        }

        if (checkin == null || checkout == null){
            throw new IllegalArgumentException("Dates must be specified");
        }

        if (checkout.isBefore(checkin)){
            throw new IllegalArgumentException("Checkin date must be before Checkout date");
        }

        if (checkin.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Checkin Date cannot be in the past");
        }

//        if (room.isBooked(checkin, checkout)){
//            throw new IllegalArgumentException("Room already booked for selected dates.");
//        }


    }

public static void validateInvoice(Guests guest, Invoices invoice,Invoices.PaymentMethod paymentMethodForReservation){

    Validator.checkNotNull(guest, "Guest is required.");
    Validator.checkNotNull(invoice, "Invoice is required.");

    //TODO
    double totalPrice = invoice.CalculateTotal();

    if (guest.getBalance() < totalPrice && (paymentMethodForReservation == Invoices.PaymentMethod.ONLINE)){
        throw new IllegalArgumentException("Insufficient balance, the required balance is " + totalPrice);
    }
}



    public static void validateRoomAssignment(Rooms room, LocalDate checkin, LocalDate checkout) {

        if (room == null) {
            throw new IllegalArgumentException("Room does not exist");
        }

        if (room.getStatus() != Rooms.RoomStatus.AVAILABLE) {
            throw new IllegalArgumentException("Room is not available");
        }

        if (room.isBooked(checkin, checkout)) {
            throw new IllegalArgumentException("Room already booked for selected dates");
        }
    }





    public static void validateCancellation(Guests guest, Reservations reservation){

        Validator.checkNotNull(reservation, "Reservation does not exist");

        Validator.checkNotNull(guest, "User must be logged in");

        if (! reservation.getGuest().equals(guest)){
            throw new IllegalArgumentException("User is not authorized to cancel this reservation.");
        }

        if (reservation.getStatus() == Reservations.ReservationStatus.COMPLETED){
            throw new IllegalArgumentException("Cannot cancel completed reservation.");
        }

        if (reservation.getStatus() == Reservations.ReservationStatus.CANCELLED){
            throw new IllegalArgumentException("Reservation is already cancelled.");
        }


    }




}
