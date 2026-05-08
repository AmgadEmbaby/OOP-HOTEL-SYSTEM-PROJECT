import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class RoomSelectionController {

    // --- FXML UI Elements ---
    @FXML private FlowPane roomContainer;
    @FXML private Label datesLabel;

    // --- State Variables passed from Dashboard ---
    private Guests currentGuest;
    private LocalDate checkIn;
    private LocalDate checkOut;

    /**
     * Receives the user data and available rooms map from the GuestDashboardController.
     */
    public void initData(Guests guest, Map<RoomType, Integer> availableRooms, LocalDate checkIn, LocalDate checkOut) {
        this.currentGuest = guest;
        this.checkIn = checkIn;
        this.checkOut = checkOut;

        // Calculate the length of stay for pricing displays
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days <= 0) days = 1; // Failsafe for same-day logic

        datesLabel.setText("FOR " + checkIn + " TO " + checkOut + " (" + days + " NIGHTS)");

        // Dynamically build a box for every room type in the Map
        for (Map.Entry<RoomType, Integer> entry : availableRooms.entrySet()) {
            RoomType roomType = entry.getKey();
            int countLeft = entry.getValue();

            // Utilize backend methods to construct the card
            VBox roomCard = createRoomBox(roomType, countLeft, days);

            // Inject the constructed box into the FlowPane
            roomContainer.getChildren().add(roomCard);
        }
    }

    /**
     * Constructs a JavaFX VBox component dynamically based on the RoomType data.
     */
    private VBox createRoomBox(RoomType room, int roomsLeft, long daysOfStay) {
        VBox box = new VBox(15);
        box.getStyleClass().add("stat-card"); // Applies your luxury CSS styling
        box.setPrefWidth(340);
        box.setPrefHeight(280);
        box.setAlignment(Pos.CENTER);

        // 1. Room Name (From backend)
        Label nameLabel = new Label(room.getTypeName().toUpperCase() + " SUITE");
        nameLabel.getStyleClass().add("stat-number");
        nameLabel.setStyle("-fx-font-size: 28px;");

        // 2. Capacity & Beds (From backend)
        Label detailsLabel = new Label(room.getCapacity() + " Guests • " + room.getNumberOfBeds() + " Beds");
        detailsLabel.getStyleClass().add("stat-label");

        // 3. Room Description (From backend)
        Label descLabel = new Label(room.getRoomDescription());
        descLabel.setStyle("-fx-text-fill: #959581; -fx-font-family: 'Montserrat Light'; -fx-font-size: 11px;");
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setMaxWidth(280);

        // 4. Pricing (From backend)
        double totalCost = room.getPricePerNight() * daysOfStay;
        Label priceLabel = new Label(String.format("$%.2f / Night (Total: $%.2f)", room.getPricePerNight(), totalCost));
        priceLabel.setStyle("-fx-text-fill: #DADED8; -fx-font-family: 'Montserrat Light'; -fx-font-size: 13px;");

        // 5. Scarcity Warning
        Label availabilityLabel = new Label("Only " + roomsLeft + " left!");
        availabilityLabel.setStyle("-fx-text-fill: #8B3A3A; -fx-font-family: 'Cinzel'; -fx-font-size: 11px;");

        // 6. Select Button
        Button selectBtn = new Button("SELECT THIS SUITE");
        selectBtn.getStyleClass().add("action-button");

        // Lambda expression to handle the handoff when this specific button is clicked
        selectBtn.setOnAction(e -> handleRoomSelection(e, room));

        // Assemble the card
        box.getChildren().addAll(nameLabel, detailsLabel, descLabel, priceLabel, availabilityLabel, selectBtn);
        return box;
    }

    /**
     * Handles the transition to the final reservation and payment screen.
     */
    private void handleRoomSelection(ActionEvent event, RoomType selectedRoom) {
        try {
            // Load the final payment screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MakeReservation.fxml"));
            Parent root = loader.load();

            // Pass the baton to the next controller
            MakeReservationController reservationController = loader.getController();
            reservationController.initData(this.currentGuest, selectedRoom, this.checkIn, this.checkOut);

            // Switch Scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 700));
            stage.show();

        } catch (Exception e) {
            System.out.println("Error loading MakeReservation.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cancels the room search and safely returns to the Dashboard.
     */
    @FXML
    private void goBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestDashboard.fxml"));
        Parent root = loader.load();

        // Ensure the Dashboard still knows who is logged in!
        GuestDashboardController dashboard = loader.getController();
        dashboard.initData(this.currentGuest);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 700));
        stage.show();
    }
}