import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.List;

public class BookingSuccessController {

    @FXML private Label reservationIdLabel;
    @FXML private Label guestNameLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label totalLabel;

    private Guests guest;
    private Reservations reservation;
    private List<Amenity> amenities;

    @FXML
    public void initialize() {
    }

    public void initData(Guests guest, Reservations reservation, List<Amenity> amenities) {
        this.guest = guest;
        this.reservation = reservation;
        this.amenities = amenities;

        loadData();
    }

    private void loadData() {

        if (reservationIdLabel != null)
            reservationIdLabel.setText("Reservation ID: " + reservation.getReservationID());

        if (guestNameLabel != null)
            guestNameLabel.setText("Guest: " + guest.getUserName());

        if (roomTypeLabel != null)
            roomTypeLabel.setText("Room: " + reservation.getTypeDesired().getTypeName());

        if (totalLabel != null)
            totalLabel.setText("Total Paid: " + reservation.getStayPrice());
    }
}