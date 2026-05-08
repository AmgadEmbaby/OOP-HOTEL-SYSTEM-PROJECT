import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML private HBox arrivalsContainer;
    @FXML private Label adminNameLabel;
    @FXML private Label totalRoomsLabel;
    @FXML private Label availableRoomsLabel;
    @FXML private Label totalGuestsLabel;
    @FXML private Label totalReservationsLabel;

    @FXML private VBox homeContent;
    @FXML private VBox activityFeedContainer;
    @FXML private StackPane contentArea;

    // This is the inner StackPane wrapping botanical + scrollpane + minimize button
    // Saving it as one unit means showHome() restores ALL layers at once
    @FXML private StackPane dashboardNode;

    private static AdminController instance;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        refreshDashboard();
        loadTonightArrivals();
    }
    private void loadTonightArrivals() {

        arrivalsContainer.getChildren().clear();

        for (Reservations r : Database.getReservationsList()) {

            VBox card = new VBox(6);

            card.setPrefWidth(180);
            card.setMinWidth(180);

            card.setStyle("""
            -fx-background-color: rgba(255,255,255,0.05);
            -fx-background-radius: 18;
            -fx-border-radius: 18;
            -fx-border-color: rgba(118,128,100,0.18);
            -fx-padding: 18;
        """);

            Label guest = new Label(r.getGuest().getUserName());
            guest.setStyle("""
            -fx-text-fill: #F3EFE6;
            -fx-font-size: 18px;
            -fx-font-family: 'Cinzel';
        """);

            //Label room = new Label("Room " + r.getRoom().getRoomNumber());

            String roomText;

            if (r.getRoom() != null) {
                roomText = "Room " + r.getRoom().getRoomNumber();
            } else {
                roomText = "Room not assigned";
            }

            Label room = new Label(roomText);

            room.setStyle("""
            -fx-text-fill: rgba(218,222,216,0.65);
            -fx-font-size: 12px;
        """);

            card.getChildren().addAll(guest, room);

            arrivalsContainer.getChildren().add(card);
        }
    }
    public void refreshDashboard() {
        loadStats();
        loadActivityFeed();
    }

    public static void refreshUI() {
        if (instance != null) instance.refreshDashboard();
    }

    private void loadStats() {
        adminNameLabel.setText(Database.getAdmin().getName());

        totalRoomsLabel.setText(String.valueOf(Database.getRoomList().size()));

        long available = Database.getRoomList().stream()
                .filter(r -> r.getStatus() == Rooms.RoomStatus.AVAILABLE)
                .count();

        availableRoomsLabel.setText(String.valueOf(available));
        totalGuestsLabel.setText(String.valueOf(Database.getGuestList().size()));
        totalReservationsLabel.setText(String.valueOf(Database.getReservationsList().size()));
    }

    private void loadActivityFeed() {
        activityFeedContainer.getChildren().clear();
        for (String a : Database.getActivityFeed()) {
            Label l = new Label("• " + a);
            l.getStyleClass().add("activity-item");
            activityFeedContainer.getChildren().add(l);
        }
    }

    // ---------- NAVIGATION ----------

    @FXML
    private void showHome(ActionEvent e) {
        // Restore the full dashboard (botanical + scroll + minimize) as one unit
        contentArea.getChildren().setAll(dashboardNode);
        refreshDashboard();
    }

    @FXML
    private void showRooms(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageRooms.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void showAmenities(ActionEvent a) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("ManageAmenities.fxml")
            );

            Parent amenitiesView = loader.load();

            // IMPORTANT: keep controller reference (for future refresh)
            ManageAmenitiesController controller = loader.getController();

            // swap view
            contentArea.getChildren().setAll(amenitiesView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showRoomTypes(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageRoomTypes.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML private void showGuests(ActionEvent e) {}
    @FXML private void showReservations(ActionEvent e) {}

    @FXML
    private void logout(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new javafx.scene.Scene(root, 1000, 600));
        stage.show();
    }

    @FXML
    private void minimizeApp(ActionEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

}
