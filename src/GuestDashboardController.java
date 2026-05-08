import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Map;

public class GuestDashboardController {

    @FXML private VBox homePane;
    @FXML private VBox reservationsPane;
    @FXML private VBox billingPane;

    @FXML private Label guestNameLabel;
    @FXML private Label balanceLabel;
    @FXML private Label activeStaysLabel;
    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private Label searchErrorLabel;

    @FXML private ListView<String> reservationsList;
    @FXML private ComboBox<String> activeStayCombo;
    @FXML private ComboBox<String> amenityCombo;
    @FXML private Label resMessageLabel;

    @FXML private ListView<String> invoicesList;
    @FXML private TextField targetInvoiceField;
    @FXML private TextField rechargeField;
    @FXML private Label billingMessageLabel;

    private Guests currentGuest;

    public void initData(Guests guest) {
        this.currentGuest = guest;
        guestNameLabel.setText(guest.getUserName());

        for (Amenity a : Database.getamenitiesList()) {
            amenityCombo.getItems().add(a.getAmenityName());
        }
        refreshData();
    }

    private void refreshData() {
        balanceLabel.setText(String.format("$%.2f", currentGuest.getBalance()));

        reservationsList.getItems().clear();
        activeStayCombo.getItems().clear();

        int activeCount = 0;

        for (Reservations res : currentGuest.getGuestReservations()) {
            String info = "ID: " + res.getReservationID() + " | " + res.getTypeDesired().getTypeName() +
                    " | " + res.getCheckin() + " to " + res.getCheckout() + " | Status: " + res.getStatus();
            reservationsList.getItems().add(info);

            if (res.getStatus().equals(Reservations.ReservationStatus.CONFIRMED) ||
                    res.getStatus().equals(Reservations.ReservationStatus.PENDING)) {

                activeStayCombo.getItems().add(res.getReservationID() + " - " + res.getTypeDesired().getTypeName());
                activeCount++;
            }
        }

        activeStaysLabel.setText(String.valueOf(activeCount));

        invoicesList.getItems().clear();
        for (Invoices inv : currentGuest.getGuestInvoices()) {
            String info = inv.getInvoiceId() + " | " + inv.getType() + " | Total: $" +
                    String.format("%.2f", inv.CalculateTotal()) + " | Status: " + inv.getStatus();
            invoicesList.getItems().add(info);
        }
    }

    @FXML private void showHome() { switchPane(homePane); }
    @FXML private void showReservations() { refreshData(); switchPane(reservationsPane); }
    @FXML private void showBilling() { refreshData(); switchPane(billingPane); }

    private void switchPane(VBox paneToShow) {
        homePane.setVisible(false);
        reservationsPane.setVisible(false);
        billingPane.setVisible(false);
        paneToShow.setVisible(true);
        paneToShow.toFront();
    }

    private int getSelectedReservationId() throws Exception {
        String selection = activeStayCombo.getValue();
        if (selection == null || selection.isEmpty()) {
            throw new Exception("Please select an active reservation first.");
        }
        return Integer.parseInt(selection.split(" - ")[0]);
    }

    @FXML
    private void handleOrderAmenity() {
        try {
            int resId = getSelectedReservationId();
            String amenity = amenityCombo.getValue();
            if (amenity == null) throw new Exception("Please select an amenity to order.");

            currentGuest.orderAmenity(resId, amenity);
            resMessageLabel.setStyle("-fx-text-fill: #768064;");
            resMessageLabel.setText(amenity + " successfully ordered to your room!");
        } catch (Exception e) {
            resMessageLabel.setStyle("-fx-text-fill: #d6b8b8;");
            resMessageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        try {
            int resId = getSelectedReservationId();
            boolean success = currentGuest.cancelReservation(resId);

            if (!success) {
                throw new Exception("Failed to cancel reservation. It may already be cancelled.");
            }

            resMessageLabel.setStyle("-fx-text-fill: #768064;");
            resMessageLabel.setText("Reservation cancelled successfully.");
            refreshData();
        } catch (Exception e) {
            resMessageLabel.setStyle("-fx-text-fill: #d6b8b8;");
            resMessageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCheckout() {
        try {
            int resId = getSelectedReservationId();
            currentGuest.requestCheckout(resId, Invoices.PaymentMethod.ONLINE);
            resMessageLabel.setStyle("-fx-text-fill: #768064;");
            resMessageLabel.setText("Checkout processed successfully.");
            refreshData();
        } catch (Exception e) {
            resMessageLabel.setStyle("-fx-text-fill: #d6b8b8;");
            resMessageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handlePayInvoice() {
        String invId = targetInvoiceField.getText().trim();
        if (invId.isEmpty()) {
            billingMessageLabel.setText("Please enter an Invoice ID.");
            return;
        }
        try {
            currentGuest.onlinePaymentForTheOngoingInvoices(invId);
            billingMessageLabel.setStyle("-fx-text-fill: #768064;");
            billingMessageLabel.setText("Payment processed.");
            refreshData();
        } catch (Exception e) {
            billingMessageLabel.setStyle("-fx-text-fill: #d6b8b8;");
            billingMessageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleRecharge() {
        try {
            int amount = Integer.parseInt(rechargeField.getText().trim());
            if (amount < 0) throw new Exception("Cannot recharge negative amount.");

            currentGuest.AddTobalance(amount);
            billingMessageLabel.setStyle("-fx-text-fill: #768064;");
            billingMessageLabel.setText("Balance recharged successfully.");
            rechargeField.clear();
            refreshData();
        } catch (Exception e) {
            billingMessageLabel.setStyle("-fx-text-fill: #d6b8b8;");
            billingMessageLabel.setText("Please enter a valid numeric amount.");
        }
    }

    @FXML
    private void searchRooms(ActionEvent event) {
        LocalDate checkIn = checkInPicker.getValue();
        LocalDate checkOut = checkOutPicker.getValue();
        searchErrorLabel.setText("");

        try {
            ReservationsValidation.validateReservation(currentGuest, checkIn, checkOut);

            Map<RoomType, Integer> availableRooms = Guests.ViewAvilableRooms(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                searchErrorLabel.setStyle("-fx-text-fill: #d6b8b8;");
                searchErrorLabel.setText("We're sorry, no rooms are available for those dates.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RoomSelection.fxml"));
            Parent root = loader.load();

            RoomSelectionController selectionController = loader.getController();
            selectionController.initData(this.currentGuest, availableRooms, checkIn, checkOut);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 700));
            stage.show();

        } catch (IllegalArgumentException e) {
            searchErrorLabel.setStyle("-fx-text-fill: #d6b8b8;");
            searchErrorLabel.setText(e.getMessage());

        } catch (Exception e) {
            searchErrorLabel.setStyle("-fx-text-fill: #d6b8b8;");
            searchErrorLabel.setText("System Error: Could not load available rooms.");
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {
        this.currentGuest = null;
        Parent root = FXMLLoader.load(getClass().getResource("/StartScreen.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }
}