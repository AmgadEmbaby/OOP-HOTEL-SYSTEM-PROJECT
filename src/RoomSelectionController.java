import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Map;

public class RoomSelectionController {

    @FXML private FlowPane roomContainer;
    @FXML private Label datesLabel;

    private Guests currentGuest;
    private LocalDate checkIn;
    private LocalDate checkOut;

    // This receives the data from the Dashboard
    public void initData(Guests guest, Map<RoomType, Integer> availableRooms, LocalDate checkIn, LocalDate checkOut) {
        this.currentGuest = guest;
        this.checkIn = checkIn;
        this.checkOut = checkOut;

        datesLabel.setText("FOR " + checkIn + " TO " + checkOut);

        // Dynamically build a box for every room type in the Map
        for (Map.Entry<RoomType, Integer> entry : availableRooms.entrySet()) {
            RoomType room = entry.getKey();
            int countLeft = entry.getValue();

            // Create the Box
            VBox roomCard = createRoomBox(room, countLeft);

            // Add the Box to the screen
            roomContainer.getChildren().add(roomCard);
        }
    }

    // This method physically constructs the UI boxes using your CSS classes
    private VBox createRoomBox(RoomType room, int roomsLeft) {
        VBox box = new VBox(15);
        box.getStyleClass().add("stat-card");
        box.setPrefWidth(320);
        box.setPrefHeight(250);
        box.setAlignment(javafx.geometry.Pos.CENTER);

        // 1. Room Name
        Label nameLabel = new Label(room.getTypeName().toUpperCase());
        nameLabel.getStyleClass().add("stat-number"); // Big text

        // 2. Capacity & Beds
        Label detailsLabel = new Label(room.getCapacity() + " Guests • " + room.getNumberOfBeds() + " Beds");
        detailsLabel.getStyleClass().add("stat-label"); // Small green text

        // 3. Price
        Label priceLabel = new Label("$" + room.getPricePerNight() + " / Night");
        priceLabel.setStyle("-fx-text-fill: #959581; -fx-font-family: 'Montserrat Light';");

        // 4. Availability Warning
        Label availabilityLabel = new Label("Only " + roomsLeft + " left!");
        availabilityLabel.setStyle("-fx-text-fill: #8B3A3A; -fx-font-family: 'Cinzel'; -fx-font-size: 10px;");

        // 5. Select Button
        Button selectBtn = new Button("SELECT THIS SUITE");
        selectBtn.getStyleClass().add("action-button");

        // When clicked, go to the final Reservation/Payment screen
        selectBtn.setOnAction(e -> handleRoomSelection(e, room));

        // Add all elements to the box
        box.getChildren().addAll(nameLabel, detailsLabel, priceLabel, availabilityLabel, selectBtn);
        return box;
    }

    // Handles what happens when they click "Select This Suite"
    private void handleRoomSelection(ActionEvent event, RoomType selectedRoom) {
        System.out.println("Guest selected: " + selectedRoom.getTypeName());

        // TODO: Load the MakeReservation.fxml (Payment Screen) here!
        // You will use the exact same pattern: Load FXML -> getController() -> pass currentGuest, selectedRoom, checkIn, checkOut -> Switch Scene.
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        // Return to Dashboard and pass the guest back so it knows who is logged in!
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestDashboard.fxml"));
        Parent root = loader.load();

        GuestDashboardController dashboard = loader.getController();
        dashboard.initData(currentGuest); // Give the dashboard the guest data back

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 700));
        stage.show();
    }
}