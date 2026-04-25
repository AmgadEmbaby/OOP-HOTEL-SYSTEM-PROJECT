import java.time.*;
public class Receptionist extends Staff {


    public void checkIn(int reservationID) {

        Reservations reservation = Database.findReservation(reservationID);
        LocalDate today = LocalDate.now();
        if (reservation != null) {
            if (today.isBefore(reservation.getCheckin())) {
                System.out.println("Guest is early, check in is scheduled for: " + reservation.getCheckin());
                return; //exist the entire function if  guest tries to check in early

            } else if (today.isAfter(reservation.getCheckin())) {
                System.out.println("Guest is late for their check in date");
                System.out.println("Proceeding with checkin...");
                //the guest is a bit late but since i found his reservation id in the system then the reservation is still available
            } else {
                System.out.println("Guest is on time WELCOME");
            }

            //PAYMENT AT CHECK IN DESK
            if (reservation.getStatus() == Reservations.ReservationStatus.PENDING) {
                if (reservation.getMethod() == Invoices.PaymentMethod.CASH) {
                    System.out.println("Please Pay by cash to confirm you reservation");
                } else if (reservation.getMethod() == Invoices.PaymentMethod.CREDIT_CARD) {
                    System.out.println("Please Pay by credit card to confirm you reservation");
                }
                reservation.setStatus(Reservations.ReservationStatus.CONFIRMED);
            }

            if (reservation.getStatus() == Reservations.ReservationStatus.CONFIRMED) {
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

                        System.out.println("room number " + r.getRoomNumber());
                        System.out.println("Guest checkin successful");
                        return;
                    }
                }
            }

            //VALIDATION??
            System.out.println("ERROR: room unavailable"); //should not happen since there is a func that checks for double booking but assuming the admin shut down a room for e.g.

        } else {
            System.out.println("Reservation ID not found");
        }
    }




        public void checkout(int reservationID,Invoices.PaymentMethod method) {

            Reservations reservation = Database.findReservation(reservationID);
            LocalDate today = LocalDate.now();

            if (reservation != null) {
                if (reservation.getRoom() != null ) {
                    if (method == Invoices.PaymentMethod.CASH || method == Invoices.PaymentMethod.CREDIT_CARD) {
                        processRoomServicePayment(reservation.getRoom().getRoomNumber(), method);
                    }
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
    }public void processRoomServicePayment(int roomNumber, Invoices.PaymentMethod method) {
        //  Find the room
        Rooms room = Database.findRoom(roomNumber);

        //  Calculate what they owe for snacks/services
        double amount = room.getTotalAmenityCost();

        if (amount > 0) {
            try {
                //  Find the guest's reservation
                Reservations activeRes = null;
                for (Reservations res : Database.getReservationsList()) {
                    if (res.getRoom() != null && res.getRoom().getRoomNumber() == roomNumber) {
                        activeRes = res;
                        break;
                    }
                }

                // 4. Create the invoice
                Invoices invoice = new Invoices(
                        amount,
                        method,
                        activeRes,
                        Invoices.InvoiceType.ROOM_SERVICE,
                        Invoices.InvoiceStatus.PAID
                );

                // save it to the invoice list and reset room bill to 0
                Database.getInvoicesList().add(invoice);
               // room.getAmenities().clear();

                System.out.println("Payment Successful via " + method);
                System.out.println("Total collected (with tax): " + invoice.CalculateTotal());

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Nothing to pay for this room.");
        }
    }






}
