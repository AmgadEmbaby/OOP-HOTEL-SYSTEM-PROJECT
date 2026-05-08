
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;




    public class AdminController implements Initializable {

        @FXML private Label adminNameLabel;
        @FXML private Label totalRoomsLabel;
        @FXML private Label availableRoomsLabel;
        @FXML private Label totalGuestsLabel;
        @FXML private Label totalReservationsLabel;
        @FXML private StackPane contentArea;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            // Set admin name
            adminNameLabel.setText(Database.getAdmin().getName());

            // Set stats
            totalRoomsLabel.setText(String.valueOf(Database.getRoomList().size()));
            availableRoomsLabel.setText(String.valueOf(
                    Database.getRoomList().stream()
                            .filter(r -> r.getStatus() == Rooms.RoomStatus.AVAILABLE)
                            .count()
            ));
            totalGuestsLabel.setText(String.valueOf(Database.getGuestList().size()));
            totalReservationsLabel.setText(String.valueOf(Database.getReservationsList().size()));
        }

        @FXML private void showHome(ActionEvent event) {}
        @FXML private void showRooms(ActionEvent event) {}
        @FXML private void showAmenities(ActionEvent event) {}
        @FXML private void showRoomTypes(ActionEvent event) {}
        @FXML private void showGuests(ActionEvent event) {}
        @FXML private void showReservations(ActionEvent event) {}

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




