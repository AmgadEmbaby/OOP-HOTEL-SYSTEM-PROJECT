import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Map;

public class GuestDashboardController {

    @FXML private StackPane contentArea;
    @FXML private Label guestNameLabel;
    @FXML private Label balanceLabel;
    @FXML private Label activeStaysLabel;
    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private Label searchErrorLabel;


    private Guests currentGuest;

    public void initData(Guests loggedInGuest) {
        this.currentGuest = loggedInGuest;

        guestNameLabel.setText(currentGuest.getUserName());
        balanceLabel.setText(String.format("%.2f", currentGuest.getBalance()));


        long activeCount = currentGuest.getGuestReservations().stream()
                .filter(r -> r.getStatus() == Reservations.ReservationStatus.CONFIRMED ||
                        r.getStatus() == Reservations.ReservationStatus.PENDING)
                .count();
        activeStaysLabel.setText(String.valueOf(activeCount));
    }


    // ==========================================
    // 1. THE ROOM SEARCH ENGINE
    // ==========================================
    @FXML
    private void searchRooms(ActionEvent event) {
        LocalDate checkIn = checkInPicker.getValue();
        LocalDate checkOut = checkOutPicker.getValue();

        // 1. Clear old errors
        searchErrorLabel.setStyle("-fx-text-fill: #d6b8b8;");
        searchErrorLabel.setText("");

        // 2. Validate the dates
        if (checkIn == null || checkOut == null) {
            searchErrorLabel.setText("Please select both Check-in and Check-out dates.");
            return;
        }
        if (checkIn.isBefore(LocalDate.now())) {
            searchErrorLabel.setText("Check-in date cannot be in the past.");
            return;
        }
        if (!checkOut.isAfter(checkIn)) {
            searchErrorLabel.setText("Check-out must be at least one day after Check-in.");
            return;
        }

        try {

            Map<RoomType, Integer> availableRooms = Guests.ViewAvilableRooms(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                searchErrorLabel.setText("We're sorry, no rooms are available for those dates.");
            } else {
                searchErrorLabel.setStyle("-fx-text-fill: #768064;"); // Olive Green Success
                searchErrorLabel.setText("Rooms found! Loading selection...");
                // 1. Prepare the loader for the Room Selection screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/RoomSelection.fxml"));
                Parent root = loader.load();

                // 2. Get the new controller and hand over all the data!
                RoomSelectionController selectionController = loader.getController();
                selectionController.initData(this.currentGuest, availableRooms, checkIn, checkOut);

                // 3. Switch the scene
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 1200, 700));
                stage.show();
                // Assuming MainGui has your makeDraggable method
                MainGui.makeDraggable(root, stage);
                System.out.println("Available Rooms Map: " + availableRooms);
            }

        } catch (Exception e) {
            searchErrorLabel.setText("An error occurred: " + e.getMessage());
        }
    }


    // ==========================================
    // 2. NAVIGATION BAR ROUTING
    // ==========================================

    @FXML
    private void showDashboard(ActionEvent event) {
        System.out.println("Already on Dashboard!");
        // If they click dashboard while deep in another menu, you would
        // reload the home FXML into the contentArea here.
    }

    @FXML
    private void showBookingFlow(ActionEvent event) {
        // If they click this from the sidebar, just focus on the date pickers
        checkInPicker.requestFocus();
    }

    @FXML
    private void showMyReservations(ActionEvent event) {
        System.out.println("Routing to View/Cancel/Add Amenity Screen...");
        // TODO: Load MyReservations.fxml into the StackPane (contentArea)
    }

    @FXML
    private void showBilling(ActionEvent event) {
        System.out.println("Routing to Invoices/Recharge Screen...");
        // TODO: Load GuestBilling.fxml into the StackPane (contentArea)
    }

    @FXML
    private void requestCheckout(ActionEvent event) {
        System.out.println("Opening Express Checkout module...");
        // TODO: Load ExpressCheckout FXML or trigger a Pop-up Dialog
    }


    // ==========================================
    // 3. SYSTEM CONTROLS
    // ==========================================

    @FXML
    private void logout(ActionEvent event) throws Exception {
        // Clear the current guest and go back to StartScreen
        this.currentGuest = null;
        Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }

    @FXML
    private void exitApp(ActionEvent event) {
        System.exit(0);
    }
}