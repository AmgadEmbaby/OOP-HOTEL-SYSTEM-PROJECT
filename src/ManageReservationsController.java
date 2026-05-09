import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;

public class ManageReservationsController {

    @FXML private VBox reservationsContainer;
    @FXML private TextField searchField;

    @FXML private Label detailGuest;
    @FXML private Label detailRoom;
    @FXML private Label detailDates;
    @FXML private Label detailStatus;
    @FXML private Label detailPayment;

    private List<Reservations> allReservations = new ArrayList<>();

    @FXML
    public void initialize() {
        allReservations.addAll(Database.getReservationsList());
        loadReservations(allReservations);
    }

    // ================= LOAD =================

    private void loadReservations(List<Reservations> list) {
        reservationsContainer.getChildren().clear();
        for (Reservations r : list) {
            reservationsContainer.getChildren().add(createRow(r));
        }
    }

    // ================= ROW CARD =================

    private VBox createRow(Reservations r) {
        Label guest = new Label(r.getGuest().getUserName().toUpperCase());
        guest.setStyle("""
            -fx-font-family: 'Cinzel';
            -fx-font-size: 13px;
            -fx-text-fill: #F3EFE6;
            -fx-letter-spacing: 1px;
        """);

        Label info = new Label(
                r.getTypeDesired().getTypeName() + "  ·  " +
                        r.getCheckin() + " → " + r.getCheckout()
        );
        info.setStyle("""
            -fx-font-family: 'Montserrat Light';
            -fx-font-size: 11px;
            -fx-text-fill: rgba(218,222,216,0.55);
        """);

        Label status = new Label(r.getStatus().toString());
        status.getStyleClass().add(
                r.getStatus() == Reservations.ReservationStatus.CONFIRMED
                        ? "status-available" : "status-occupied"
        );

        VBox row = new VBox(5, guest, info, status);
        row.setPadding(new Insets(14, 16, 14, 16));
        row.setStyle("""
            -fx-background-color: rgba(255,255,255,0.03);
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: rgba(118,128,100,0.14);
            -fx-border-width: 0.7;
            -fx-cursor: hand;
        """);

        row.setOnMouseEntered(e -> row.setStyle("""
            -fx-background-color: rgba(255,255,255,0.06);
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: rgba(218,222,216,0.18);
            -fx-border-width: 0.7;
            -fx-cursor: hand;
        """));

        row.setOnMouseExited(e -> row.setStyle("""
            -fx-background-color: rgba(255,255,255,0.03);
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: rgba(118,128,100,0.14);
            -fx-border-width: 0.7;
            -fx-cursor: hand;
        """));

        row.setOnMouseClicked(e -> showDetails(r));

        return row;
    }

    // ================= DETAILS =================

    private void showDetails(Reservations r) {
        detailGuest.setText(r.getGuest().getUserName());
        detailRoom.setText(r.getTypeDesired().getTypeName().toUpperCase());
        detailDates.setText(r.getCheckin() + "  →  " + r.getCheckout());
        detailStatus.setText(r.getStatus().toString());
        detailPayment.setText(r.getMethod().toString());
    }

    // ================= FILTER =================

    @FXML
    private void filterReservations() {
        String text = searchField.getText().trim().toLowerCase();

        if (text.isBlank()) {
            loadReservations(allReservations);
            return;
        }

        List<Reservations> filtered = new ArrayList<>();
        for (Reservations r : allReservations) {
            boolean match =
                    r.getGuest().getUserName().toLowerCase().contains(text) ||
                            r.getStatus().toString().toLowerCase().contains(text) ||
                            r.getTypeDesired().getTypeName().toLowerCase().contains(text);
            if (match) filtered.add(r);
        }
        loadReservations(filtered);
    }

    // ================= REFRESH =================

    public void refresh() {
        allReservations.clear();
        allReservations.addAll(Database.getReservationsList());
        loadReservations(allReservations);
    }
}