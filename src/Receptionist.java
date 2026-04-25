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
                        for (Invoices inv : Database.getInvoicesList()) {
                            if (inv.getReservation().getReservationID() == reservationID &&
                                    inv.getStatus() == Invoices.InvoiceStatus.UNPAID) {

                                inv.setStatus(Invoices.InvoiceStatus.PAID);
                                System.out.println("Payment settled at front desk for Invoice: " + inv.getInvoiceId());
                            }
                        }
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
        LocalDateTime now = LocalDateTime.now();

        if (res != null) {
            LocalDateTime deadline = res.getCheckin().atTime(18, 0);

            if (now.isAfter(deadline) && res.getStatus() == Reservations.ReservationStatus.PENDING) {

                if (res.getMethod() != Invoices.PaymentMethod.ONLINE) {

                    // CANCEL THE ORIGINAL UNPAID BOOKING INVOICE ---
                    for (Invoices inv : Database.getInvoicesList()) {
                        if (inv.getReservation().getReservationID() == reservationID &&
                                inv.getType() == Invoices.InvoiceType.BOOKING) {

                            inv.setStatus(Invoices.InvoiceStatus.CANCELLED);
                        }
                    }

                    //  Calculate the penalty
                    double penalty = res.calculateCancellationFee();
                    res.getGuest().setBalance(res.getGuest().getBalance() - penalty);

                    try {
                        //  CREATE THE PENALTY INVOICE WITH ENUMS ---
                        Invoices penaltyInvoice = new Invoices(
                                penalty,
                                res.getMethod(),
                                res,
                                Invoices.InvoiceType.PENALTY,
                                Invoices.InvoiceStatus.PAID
                        );

                        Database.getInvoicesList().add(penaltyInvoice);
                        System.out.println("Penalty Invoice " + penaltyInvoice.getInvoiceId() + " generated.");

                    } catch (InvalidPaymentException e) {
                        System.out.println("Error recording penalty: " + e.getMessage());
                    }
                }

                // Free the room and cancel the reservation
                if (res.getRoom() != null) {
                    res.getRoom().setStatus(Rooms.RoomStatus.AVAILABLE);
                }
                res.setStatus(Reservations.ReservationStatus.CANCELLED);
            }
        }
    }
}
