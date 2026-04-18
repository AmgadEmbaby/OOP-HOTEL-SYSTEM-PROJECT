import java.time.*;
public class Receptionist extends Staff {


    public void checkIn(int reservationID) {

        Reservations reservation = Database.findReservation(reservationID);
        LocalDate today = LocalDate.now();
        if (reservation != null) {

            if (today.isBefore(reservation.getCheckin())) {
                System.out.println("Guest is early for their check in date");
            } else if (today.isAfter(reservation.getCheckin())) {
                System.out.println("Guest is late for their check in date");
            } else {
                for (Rooms r : Database.getRoomList()) {
                    if (r.getRoomtype().getTypeName().equalsIgnoreCase(reservation.getTypeDesired().getTypeName())
                            && r.getStatus() == Rooms.RoomStatus.AVAILABLE) {
                        reservation.setRoom(r);
                        r.setStatus(Rooms.RoomStatus.OCCUPIED);
                        System.out.println("Guest checkin successful");
                        System.out.println("room number " + r.getRoomNumber());
                        return;
                    }
                } //VALIDATION??
            System.out.println("ERROR: room unavailable"); //should not happen since there is a func that checks for double booking but assuming the admin shut down a room for e.g.


            }


        } else {
            System.out.println("Reservation ID not found");
        }
    }
}
