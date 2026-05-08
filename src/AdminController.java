import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    // Dashboard labels
    @FXML private Label adminNameLabel;
    @FXML private Label totalRoomsLabel;
    @FXML private Label availableRoomsLabel;
    @FXML private Label totalGuestsLabel;
    @FXML private Label totalReservationsLabel;
    @FXML private VBox homeContent;
    @FXML private VBox activityFeedContainer;
    // Main content container
    @FXML private StackPane contentArea;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;

        loadDashboardStats();
        loadActivityFeed();
    }

    // ---------------- DASHBOARD ----------------

    private void loadDashboardStats() {

        adminNameLabel.setText(Database.getAdmin().getName());

        totalRoomsLabel.setText(String.valueOf(Database.getRoomList().size()));

        availableRoomsLabel.setText(String.valueOf(
                Database.getRoomList().stream()
                        .filter(r -> r.getStatus() == Rooms.RoomStatus.AVAILABLE)
                        .count()
        ));

        totalGuestsLabel.setText(String.valueOf(Database.getGuestList().size()));

        totalReservationsLabel.setText(String.valueOf(Database.getReservationsList().size()));
    }

    // ---------------- ACTIVITY FEED ----------------
    private static AdminController instance;

    private void loadActivityFeed() {
        if (activityFeedContainer == null) return;
        activityFeedContainer.getChildren().clear();
        for (String activity : Database.getActivityFeed()) {
            Label label = new Label("· " + activity);
            label.getStyleClass().add("activity-item");
            label.setWrapText(true);
            activityFeedContainer.getChildren().add(label);
        }
    }
    public void refreshDashboard() {
        loadDashboardStats();
        loadActivityFeed();
    }

    public static void refreshUI() {
        if (instance != null) {
            instance.refreshDashboard();
        }
    }
    // ---------------- NAVIGATION ----------------


    @FXML
    private void showHome(ActionEvent event) {
        try {


            refreshDashboard();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(homeContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void showRooms(ActionEvent event) {
        try {
            Parent roomsView = FXMLLoader.load(getClass().getResource("ManageRooms.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(roomsView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showAmenities(ActionEvent event) {
        // later: swap to Amenities view
    }

    @FXML
    private void showRoomTypes(ActionEvent event) {
        // later: swap to RoomTypes view
    }

    @FXML
    private void showGuests(ActionEvent event) {
        // later: read-only view
    }

    @FXML
    private void showReservations(ActionEvent event) {
        // later: read-only view
    }

    // ---------------- QUICK ACTIONS ----------------

    @FXML
    private void addRoom(ActionEvent event) {
        // ONLY navigation or popup trigger
        // Admin.createRoom() happens inside popup controller later
    }

    @FXML
    private void addAmenity(ActionEvent event) {
        // open form
    }

    @FXML
    private void addRoomType(ActionEvent event) {
        // open form
    }

    // ---------------- SYSTEM ACTIONS ----------------

    @FXML
    private void logout(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 600);
        MainGui.makeDraggable(root, stage);
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void minimizeApp(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(false);
    }
}