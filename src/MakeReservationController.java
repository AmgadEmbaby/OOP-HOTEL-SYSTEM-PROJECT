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

    // --- FXML UI Elements ---
    @FXML private Label guestNameLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label datesLabel;
    @FXML private Label totalCostLabel;
    @FXML private ComboBox<String> paymentCombo;
    @FXML private Label errorLabel;

    // --- Data Variables ---
    private Guests currentGuest;
    private RoomType selectedRoom;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public void initData(Guests guest, RoomType room, LocalDate checkIn, LocalDate checkOut) {
        this.currentGuest = guest;
        this.selectedRoom = room;
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;

        // Calculate the total price:
        long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days <= 0) days = 1;
        double totalCost = days * room.getPricePerNight();

        // 1. Update the UI labels with the summary and price
        if (guestNameLabel != null) guestNameLabel.setText(guest.getUserName());
        if (roomTypeLabel != null) roomTypeLabel.setText(room.getTypeName().toUpperCase() + " SUITE");
        if (datesLabel != null) datesLabel.setText(checkIn + "  to  " + checkOut + " (" + days + " Nights)");

        // Formatting to 2 decimal places for a professional look
        if (totalCostLabel != null) totalCostLabel.setText(String.format("$%.2f", totalCost));

        // 2. Populate the Payment Dropdown with your Enum options
        if (paymentCombo != null) paymentCombo.getItems().addAll("ONLINE", "CASH", "CREDIT CARD");
    }

    @FXML
    private void handleConfirmReservation(ActionEvent event) {
        // Clear previous errors
        errorLabel.setStyle("-fx-text-fill: #d6b8b8;");
        errorLabel.setText("");

        String selectedPayment = paymentCombo.getValue();

        if (selectedPayment == null) {
            errorLabel.setText("Please select a payment method.");
            return;
        }

        // Convert the String from the dropdown into your backend Enum
        Invoices.PaymentMethod methodEnum;
        if (selectedPayment.equals("ONLINE")) {
            methodEnum = Invoices.PaymentMethod.ONLINE;
        } else if (selectedPayment.equals("CASH")) {
            methodEnum = Invoices.PaymentMethod.CASH;
        } else {
            methodEnum = Invoices.PaymentMethod.CREDIT_CARD;
        }

        try {
            // Call the heavy-lifting backend method you already wrote!
            currentGuest.makeReservation(currentGuest, selectedRoom, checkInDate, checkOutDate, methodEnum);

            // If we get here, the backend didn't throw an error. Success!
            errorLabel.setStyle("-fx-text-fill: #768064;"); // Olive Green
            errorLabel.setText("Reservation Confirmed! Redirecting...");

            // Route them back to their dashboard after a successful booking
            returnToDashboard(event);

        } catch (Exception e) {
            // This perfectly catches your backend errors (e.g., "Insufficient balance")
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        // Just go back to the dashboard if they cancel the booking process
        returnToDashboard(event);
    }

    // Helper method so we don't write the scene-switching code twice
    private void returnToDashboard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestDashboard.fxml"));
        Parent root = loader.load();

        GuestDashboardController dashboard = loader.getController();
        dashboard.initData(currentGuest); // Give the dashboard the updated guest data (new balance, etc.)

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 700));
        stage.show();
    }
}