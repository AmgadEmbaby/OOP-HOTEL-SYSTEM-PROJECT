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
import java.time.LocalDate;
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
    @FXML private VBox contentArea;
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

        System.out.println("Reservations count: "
                + Database.getReservationsList().size());

        for (Reservations r : Database.getReservationsList()) {

            System.out.println("Reservation check-in: "
                    + r.getCheckin());

            System.out.println("Today's date: "
                    + LocalDate.now());

            // ONLY today's arrivals
            if (!r.getCheckin().equals(LocalDate.now())) {
                continue;
            }

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

            Label guest = new Label(
                    r.getGuest().getUserName()
            );

            guest.setStyle("""
            -fx-text-fill: #F3EFE6;
            -fx-font-size: 18px;
            -fx-font-family: 'Cinzel';
        """);

            Label roomType = new Label(
                    r.getTypeDesired()
                            .getTypeName()
                            .toUpperCase()
            );

            roomType.setStyle("""
            -fx-text-fill: rgba(218,222,216,0.70);
            -fx-font-size: 11px;
            -fx-letter-spacing: 2px;
        """);

            Label stay = new Label(
                    r.getCheckin()
                            + " → " +
                            r.getCheckout()
            );

            stay.setStyle("""
            -fx-text-fill: rgba(218,222,216,0.45);
            -fx-font-size: 11px;
        """);

            card.getChildren().addAll(
                    guest,
                    roomType,
                    stay
            );

            arrivalsContainer.getChildren().add(card);
        }

        // EMPTY STATE
        if (arrivalsContainer.getChildren().isEmpty()) {

            Label empty = new Label(
                    "No arrivals scheduled for tonight."
            );

            empty.setStyle("""
            -fx-text-fill: rgba(218,222,216,0.45);
            -fx-font-size: 13px;
            -fx-padding: 20;
        """);

            arrivalsContainer.getChildren().add(empty);
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
    @FXML
    private void showGuests(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageGuests.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void showReservations(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("ManageReservations.fxml")
            );

            Parent view = loader.load();

            contentArea.getChildren().setAll(view);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
