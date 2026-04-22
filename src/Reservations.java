import java.time.LocalDate;
public class Reservations {
    private Guests guest;
    private Rooms room;
    private RoomType typeDesired;
    private LocalDate checkin;
    private LocalDate checkout;
    private ReservationStatus status;
    private static int idCounter=1000;
    private int reservationID;
    private Invoices.PaymentMethod method;

    public Reservations(Guests guest, RoomType roomType, LocalDate in, LocalDate out,Invoices.PaymentMethod method) throws Exception {
        if (!out.isAfter(in)) {
            throw new Exception("Check-out must be after check-in.");
        }
        this.typeDesired= roomType;
        this.reservationID=idCounter++;
        this.guest = guest;
        this.room = null;
        this.checkin = in;
        this.checkout = out;
        this.status=ReservationStatus.PENDING;
        this.method = method;

    }

    public enum ReservationStatus {
     PENDING, CONFIRMED, CANCELLED, COMPLETED
    }


    public void confirmReservation() {
        this.status = ReservationStatus.CONFIRMED;

    }

    public void cancelReservation() {
        this.status = ReservationStatus.CANCELLED;
        this.room.setStatus(Rooms.RoomStatus.AVAILABLE);
    }

    public void checkInGuest() {
        this.room.setStatus(Rooms.RoomStatus.OCCUPIED);
    }


    public void setCheckOut(LocalDate newOutDate) throws Exception {
        if (!newOutDate.isAfter(this.checkin)) {
            throw new Exception("Error: The new check-out date is invalid.");
        }
        this.checkout = newOutDate;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Guests getGuest() { return guest; }

    public Rooms getRoom() { return room;}

    public LocalDate getCheckin() { return checkin;}

    public ReservationStatus getStatus() { return status;}

    public LocalDate getCheckout() {return checkout;}

    public Invoices.PaymentMethod getMethod() {return method;}

    public int getReservationID() {
        return reservationID;
    }

    public RoomType getTypeDesired() {
        return typeDesired;
    }

    public void displayReservation() {
        System.out.println("--- RESERVATION DETAILS ---");
        System.out.println("Reservation ID: " + this.getReservationID());
        System.out.println("Guest: " + guest.getUserName());
        System.out.println("Room: " + room.getRoomNumber());
        System.out.println("Status: " + this.status);
        System.out.println("Dates: " + checkin + " to " + checkout);
        System.out.println("Physical Room State: " + room.getStatus());
        System.out.println("---------------------------");
    }
}
