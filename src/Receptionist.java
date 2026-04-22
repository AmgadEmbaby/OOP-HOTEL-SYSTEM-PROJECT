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


    public void checkout(int reservationID) {
        System.out.println("Please enter reservation ID");
        Reservations reservation = Database.findReservation(reservationID);
        LocalDate today = LocalDate.now();

        public void checkout(int reservationID) {
            Reservations reservation = Database.findReservation(reservationID);
            LocalDate today = LocalDate.now();

            if (reservation != null) {
                double fine = reservation.calculateCheckoutFine(today);

                if (fine > 0) {
                    double currentBalance = reservation.getGuest().getBalance();
                    reservation.getGuest().setBalance(currentBalance + fine);
                    System.out.println("Checkout policy activated. Fine of $" + fine + " applied.");
                }


                if (reservation.getRoom() != null) {
                    reservation.getRoom().setStatus(Rooms.RoomStatus.AVAILABLE);
                }

                reservation.setStatus(Reservations.ReservationStatus.COMPLETED);
                System.out.println("Guest checkout successful.");

            } else {
                System.out.println("Reservation ID not found");
            }
        }

        public void releaseLateRooms() {
            LocalDate today = LocalDate.now();

            for (Reservations res : Database.getReservationsList()) {
                if (res.getMethod() == Invoices.PaymentMethod.ONLINE &&
                        res.getStatus() == Reservations.ReservationStatus.CONFIRMED &&
                        today.isAfter(res.getCheckin())) {

                    if (res.getRoom() != null) {
                        res.getRoom().setStatus(Rooms.RoomStatus.AVAILABLE);
                    }

                    res.setStatus(Reservations.ReservationStatus.CANCELLED);

                    System.out.println("Online reservation " + res.getReservationID() + " released. Guest failed to show up.");
                }
            }
        }

    public void handleNoShow(int reservationID) {
        Reservations res = Database.findReservation(reservationID);
        LocalDate today = LocalDate.now();

        if (res != null && today.isAfter(res.getCheckin()) && res.getStatus() == Reservations.ReservationStatus.PENDING) {

            if (res.getMethod() != Invoices.PaymentMethod.ONLINE) {
                double penalty = res.getTypeDesired().getPricePerNight() * 0.5;
                res.getGuest().setBalance(res.getGuest().getBalance() - penalty);
            }

            res.setStatus(Reservations.ReservationStatus.CANCELLED);
        }
    }
}
