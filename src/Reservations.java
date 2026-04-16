import java.time.LocalDate;
public class Reservations {
    private Guests guest;
    private Rooms room;
    private LocalDate checkin;
    private LocalDate checkout;
    private ReservationStatus status;

    public Reservations(Guests guest, Rooms room, LocalDate in, LocalDate out) throws Exception {
        if (!out.isAfter(in)) {
            throw new Exception("Check-out must be after check-in.");
        }
        this.guest = guest;
        this.room = room;
        this.checkin = in;
        this.checkout = out;
        this.status=ReservationStatus.pending;
    }

    public enum ReservationStatus{
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }

    public void setCheckOut(LocalDate newOutDate) throws Exception {
        if (!newOutDate.isAfter(this.checkin)) {
            throw new Exception("Error: The new check-out date is invalid.");
        }
        this.checkout = newOutDate;
    }

    public Guests getGuest() {
        return guest;
    }

    public Rooms getRoom() {
        return room;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void viewReservationDetails() {
        System.out.println("\n========== RESERVATION SUMMARY ==========");
        System.out.println("GUEST:    " + this.guest.getGuest());
        System.out.println("ROOM:     " + this.room.getRoomNumber() + " (" + this.room.getRoomType() + ")");
        System.out.println("DATES:    " + this.checkin + " to " + this.checkout);
        System.out.println("STATUS:   " + this.status);
        System.out.println("=========================================\n");
    }

    public void cancelReservation() throws Exception {
        // Real-world rule: You can't cancel a stay that is already finished!
        if (this.status == ReservationStatus.COMPLETED) {
            throw new Exception("Error: Cannot cancel a completed reservation.");
        }

        this.status = ReservationStatus.CANCELLED;

        this.room.setStatus(Rooms.IsAvalaible);

        System.out.println("Reservation for " + this.guest.getName() + " has been cancelled.");
    }
}
