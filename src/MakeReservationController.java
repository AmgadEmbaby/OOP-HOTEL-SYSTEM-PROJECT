import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MakeReservationController {

    @FXML private Label guestNameLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label datesLabel;
    @FXML private Label totalCostLabel;
    @FXML private ComboBox<String> paymentCombo;
    @FXML private Label errorLabel;

    private Guests currentGuest;
    private RoomType selectedRoom;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public void initData(Guests guest, RoomType room, LocalDate checkIn, LocalDate checkOut) {
        this.currentGuest = guest;
        this.selectedRoom = room;
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;

        long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days <= 0) days = 1;
        double totalCost = days * room.getPricePerNight();

        if (guestNameLabel != null) guestNameLabel.setText(guest.getUserName());
        if (roomTypeLabel != null) roomTypeLabel.setText(room.getTypeName().toUpperCase() + " SUITE");
        if (datesLabel != null) datesLabel.setText(checkIn + "  to  " + checkOut + " (" + days + " Nights)");

        if (totalCostLabel != null) totalCostLabel.setText(String.format("$%.2f", totalCost));

        if (paymentCombo != null) paymentCombo.getItems().addAll("ONLINE", "CASH", "CREDIT CARD");
    }

    @FXML
    private void handleConfirmReservation(ActionEvent event) {
        errorLabel.setStyle("-fx-text-fill: #d6b8b8;");
        errorLabel.setText("");

        String selectedPayment = paymentCombo.getValue();

        if (selectedPayment == null) {
            errorLabel.setText("Please select a payment method.");
            return;
        }

        Invoices.PaymentMethod methodEnum;
        if (selectedPayment.equals("ONLINE")) {
            methodEnum = Invoices.PaymentMethod.ONLINE;
        } else if (selectedPayment.equals("CASH")) {
            methodEnum = Invoices.PaymentMethod.CASH;
        } else {
            methodEnum = Invoices.PaymentMethod.CREDIT_CARD;
        }

        try {
            currentGuest.makeReservation(currentGuest, selectedRoom, checkInDate, checkOutDate, methodEnum);

            errorLabel.setStyle("-fx-text-fill: #768064;");
            errorLabel.setText("Reservation Confirmed! Redirecting...");

            returnToDashboard(event);

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        returnToDashboard(event);
    }

    private void returnToDashboard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestDashboard.fxml"));
        Parent root = loader.load();

        GuestDashboardController dashboard = loader.getController();
        dashboard.initData(currentGuest);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 700));
        stage.show();
    }
}