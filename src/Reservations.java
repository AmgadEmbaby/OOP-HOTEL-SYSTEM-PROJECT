public class Reservations {
    private Guests guest;
    private Rooms room;
    private Date checkin;
    private Date checkout;
    private ReservationStatus status;

    public Reservations(Guests guest, Rooms room, Date in, Date out) throws Exception {
        if (!checkout.isAfter(checkin)) {
            throw new Exception("Check-out must be after check-in.");
        }
        this.guest = guest;
        this.room = room;
        this.checkin = in;
        this.checkout = out;
        this.status=ReservationStatus.pending;
    }

    public enum ReservationStatus{
        pending ,
        confirmed,
        cancelled,
        completed
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }

    public void setCheckOut(Date newOutDate) throws Exception {
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

    public Date getCheckin() {
        return checkin;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Date getCheckout() {
        return checkout;
    }
}
