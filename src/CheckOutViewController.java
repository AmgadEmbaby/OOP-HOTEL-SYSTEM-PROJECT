import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CheckOutViewController {

    @FXML private TextField reservationIdField;
    @FXML private Label messageLabel;
    @FXML private Label farewellLabel;
    @FXML private VBox reservationInfoBox;
    @FXML private Label guestNameLabel;
    @FXML private Label roomNumberLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label checkInDateLabel;
    @FXML private Label checkOutDateLabel;
    @FXML private Label stayCostLabel;
    @FXML private Label amenityFeesLabel;
    @FXML private Label finesLabel;
    @FXML private Label totalLabel;

    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private VBox successBox;
    @FXML private StackPane rootPane;

    private Reservations currentReservation;
    private double calculatedTotal = 0;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    @FXML
    public void initialize() {
        paymentMethodComboBox.getItems().addAll("CASH", "CREDIT CARD", "DEBIT CARD");
    }

    @FXML
    private void handleValidate() {
        try {
            int id = Integer.parseInt(reservationIdField.getText().trim());
            currentReservation = Database.findReservation(id);

            if (currentReservation == null) {
                showMessage("RESERVATION NOT FOUND", "error");
                return;
            }

            if (currentReservation.getStatus() == Reservations.ReservationStatus.CANCELLED) {
                showMessage("THIS RESERVATION HAS BEEN CANCELLED", "error");
                return;
            }

            if (currentReservation.getStatus() == Reservations.ReservationStatus.COMPLETED) {
                showMessage("GUEST HAS ALREADY CHECKED OUT", "error");
                return;
            }

            if (currentReservation.getStatus() != Reservations.ReservationStatus.CONFIRMED) {
                showMessage("RESERVATION IS NOT CONFIRMED — PLEASE COMPLETE CHECK-IN FIRST", "error");
                return;
            }

            if (currentReservation.getRoom() == null) {
                showMessage("NO ROOM ASSIGNED TO THIS RESERVATION", "error");
                return;
            }

            // ✅ All checks passed — now calculate billing
            double stayPrice     = currentReservation.getStayPrice();

            double totalAmenities = 0;
            for (Amenity a : currentReservation.getRoom().getAmenities()) {
                totalAmenities += a.getAmenityCost();
            }

            double fine      = currentReservation.calculateCheckoutFine(LocalDate.now());
            double subtotal  = stayPrice + totalAmenities + fine;
            double tax       = subtotal * 0.14;
            this.calculatedTotal = subtotal + tax;

            guestNameLabel.setText(currentReservation.getGuest().getUserName().toUpperCase());
            roomNumberLabel.setText("ROOM " + currentReservation.getRoom().getRoomNumber());
            roomTypeLabel.setText(currentReservation.getRoom().getRoomtype().getTypeName().toUpperCase());
            checkInDateLabel.setText(currentReservation.getCheckin().format(FMT));
            checkOutDateLabel.setText(currentReservation.getCheckout().format(FMT));
            stayCostLabel.setText(String.format("$%.2f", stayPrice));
            amenityFeesLabel.setText(String.format("$%.2f", totalAmenities));
            finesLabel.setText(String.format("$%.2f", fine));
            totalLabel.setText(String.format("$%.2f", this.calculatedTotal));

            hide(messageLabel);
            show(reservationInfoBox);

        } catch (NumberFormatException e) {
            showMessage("INVALID ID FORMAT", "error");
        }
    }

    @FXML
    private void handleFinalize() {
        if (currentReservation == null) return;
        if (paymentMethodComboBox.getSelectionModel().getSelectedItem() == null) {
            showMessage("Please select a payment method.", "error");
            return;
        }

        String selected = paymentMethodComboBox.getValue();
        Invoices.PaymentMethod method = switch (selected) {
            case "CREDIT CARD" -> Invoices.PaymentMethod.CREDIT_CARD;
            case "CASH"        -> Invoices.PaymentMethod.CASH;
            default            -> Invoices.PaymentMethod.ONLINE;
        };

        try {
            Invoices checkoutInvoice = new Invoices(
                    this.calculatedTotal,
                    method,
                    currentReservation,
                    Invoices.InvoiceType.BOOKING,
                    Invoices.InvoiceStatus.PAID
            );
            Database.getInvoicesList().add(checkoutInvoice);
        } catch (Exception e) {
            System.out.println("Invoice error: " + e.getMessage());
        }

        currentReservation.setStatus(Reservations.ReservationStatus.COMPLETED);
        if (currentReservation.getRoom() != null) {
            currentReservation.getRoom().setStatus(Rooms.RoomStatus.AVAILABLE);
            currentReservation.getRoom().getAmenities().clear();
        }

        hide(reservationInfoBox);
        show(successBox);
        farewellLabel.setText("Thank you, " + currentReservation.getGuest().getUserName() + "! Safe travels.");
    }

    @FXML
    private void handleBack() {
        resetUI();
    }

    private void showMessage(String text, String type) {
        messageLabel.setText(text);
        messageLabel.setStyle(type.equals("error") ? "-fx-text-fill: #ff6b6b;" : "-fx-text-fill: #d4af37;");
        show(messageLabel);
    }

    private void show(Node n) {
        n.setVisible(true);
        n.setManaged(true);
        FadeTransition ft = new FadeTransition(Duration.millis(400), n);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private void hide(Node n) {
        if (n != null) {
            n.setVisible(false);
            n.setManaged(false);
        }
    }

    @FXML
    private void resetUI() {
        reservationIdField.clear();
        hide(reservationInfoBox);
        hide(successBox);
        hide(messageLabel);
    }
}