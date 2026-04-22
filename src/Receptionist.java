import java.time.*;
public class Receptionist extends Staff {


    public void checkIn(int reservationID) {
        System.out.println("Please enter reservation ID");
        Reservations reservation = Database.findReservation(reservationID);
        LocalDate today = LocalDate.now();
        if (reservation != null) {

            if (today.isBefore(reservation.getCheckin())) {
                System.out.println("Guest is early for their check in date");
            } else if (today.isAfter(reservation.getCheckin())) {
                System.out.println("Guest is late for their check in date");
            } else {


                if( reservation.getStatus()==Reservations.ReservationStatus.PENDING){

                }


                for (Rooms r : Database.getRoomList()) {
                    if (r.getRoomtype().getTypeName().equalsIgnoreCase(reservation.getTypeDesired().getTypeName())
                            && r.getStatus() == Rooms.RoomStatus.AVAILABLE) {
                        reservation.setRoom(r);
                        r.setStatus(Rooms.RoomStatus.OCCUPIED);
                        System.out.println("room number " + r.getRoomNumber());
                        System.out.println("Guest checkin successful");
                        return;
                    }
                } //VALIDATION??
                System.out.println("ERROR: room unavailable"); //should not happen since there is a func that checks for double booking but assuming the admin shut down a room for e.g.


            }


        } else {
            System.out.println("Reservation ID not found");
        }
    }


    public void checkout(int reservationID) {
        System.out.println("Please enter reservation ID");
        Reservations reservation = Database.findReservation(reservationID);
        LocalDate today = LocalDate.now();

        if (reservation != null) {
            double currentBalance = reservation.getGuest().getBalance();
            if (today.isBefore(reservation.getCheckout())) {
                System.out.println("Guest is early for their check out date");
                System.out.println("Early check out policy activated");
                reservation.getGuest().setBalance(currentBalance + 70.0);
                System.out.println("an early checkout fine of 70$ was added to the Guest's balance");

            } else if (today.isAfter(reservation.getCheckout())) {
                System.out.println("Guest is late for their check out date");
                System.out.println("Late check out policy activated");
                reservation.getGuest().setBalance(currentBalance + 100.0);
                System.out.println("an early checkout fine of 100$ was added to the Guest's balance");

            }

                reservation.getRoom().setStatus(Rooms.RoomStatus.AVAILABLE);
                reservation.setStatus(Reservations.ReservationStatus.COMPLETED);



        } else {
            System.out.println("Reservation ID not found");
        }
    }


    public void payAtCheckin(int reservationID, String choice ){
        Reservations r =Database.findReservation(reservationID);
        if(r.getStatus() == Reservations.ReservationStatus.PENDING){

        }

    }

}
