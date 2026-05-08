import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;

public class RoomSelectionController {

    @FXML private FlowPane roomContainer;
    @FXML private Label datesLabel;

    private Guests currentGuest;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public void initData(Guests guest, Map<RoomType, Integer> availableRooms, LocalDate checkIn, LocalDate checkOut) {
        this.currentGuest = guest;
        this.checkIn = checkIn;
        this.checkOut = checkOut;

        if (datesLabel != null) {
            datesLabel.setText("FOR " + checkIn + " TO " + checkOut);
        }

        roomContainer.getChildren().clear();

        for (Map.Entry<RoomType, Integer> entry : availableRooms.entrySet()) {
            RoomType room = entry.getKey();
            int countLeft = entry.getValue();

            VBox roomCard = createRoomBox(room, countLeft);
            roomContainer.getChildren().add(roomCard);
        }
    }

    private VBox createRoomBox(RoomType room, int roomsLeft) {
        VBox box = new VBox(15);
        box.getStyleClass().add("stat-card");
        box.setPrefWidth(320);
        // FIX: Allow the box to expand if the new Room Type name is long
        box.setMinHeight(Region.USE_PREF_SIZE);
        box.setAlignment(javafx.geometry.Pos.CENTER);
        box.setStyle("-fx-cursor: hand;"); // Make it obvious it's clickable

        Label nameLabel = new Label(room.getTypeName().toUpperCase());
        nameLabel.getStyleClass().add("stat-number");

        Label detailsLabel = new Label(room.getCapacity() + " Guests • " + room.getNumberOfBeds() + " Beds");
        detailsLabel.getStyleClass().add("stat-label");

        Label priceLabel = new Label("$" + room.getPricePerNight() + " / Night");
        priceLabel.setStyle("-fx-text-fill: #959581; -fx-font-family: 'Montserrat Light';");

        Label availabilityLabel = new Label("Only " + roomsLeft + " left!");
        availabilityLabel.setStyle("-fx-text-fill: #8B3A3A; -fx-font-family: 'Cinzel'; -fx-font-size: 10px;");

        Button selectBtn = new Button("SELECT THIS SUITE");
        selectBtn.getStyleClass().add("action-button");
        selectBtn.setMaxWidth(Double.MAX_VALUE);

        // FIX: Make BOTH the button AND the entire card clickable
        selectBtn.setOnAction(e -> handleRoomSelection(e, room));
        box.setOnMouseClicked(e -> {
            if (!(e.getTarget() instanceof Button)) {
                handleRoomSelection(new ActionEvent(box, box), room);
            }
        });

        box.getChildren().addAll(nameLabel, detailsLabel, priceLabel, availabilityLabel, selectBtn);
        return box;
    }

    private void handleRoomSelection(ActionEvent event, RoomType selectedRoom) {
        try {
            URL resource = getClass().getResource("/BookingScreen.fxml");
            if (resource == null) {
                resource = getClass().getResource("BookingScreen.fxml");
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            BookingScreenController controller = loader.getController();
            controller.initData(currentGuest, selectedRoom, checkIn, checkOut);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "System Error loading booking screen:\n" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestDashboard.fxml"));
        if (loader.getLocation() == null) loader = new FXMLLoader(getClass().getResource("GuestDashboard.fxml"));

        Parent root = loader.load();

        GuestDashboardController dashboard = loader.getController();
        dashboard.initData(currentGuest);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 700));
        stage.show();
    }
}