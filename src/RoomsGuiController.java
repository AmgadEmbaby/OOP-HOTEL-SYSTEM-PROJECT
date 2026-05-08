import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomsGuiController implements Initializable {


    @FXML private TextField floorField;
    @FXML private ComboBox<String> roomTypeCombo;
    @FXML private Label floorError;
    @FXML private Label roomTypeError;
    @FXML private Label addRoomMessage;


    @FXML private VBox roomsContainer;
    @FXML private Label roomCountLabel;

    @FXML private TextField updateRoomNumberField;
    @FXML private ComboBox<Rooms.RoomStatus> statusCombo;
    @FXML private Label updateStatusMessage;

    @FXML private TextField deleteRoomNumberField;
    @FXML private Label deleteRoomMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        refreshRoomTypeCombo();

        statusCombo.getItems().setAll(Rooms.RoomStatus.values());
        statusCombo.getSelectionModel().selectFirst();

        refreshRoomList();
    }


    @FXML
    private void handleAddRoom(ActionEvent event) {
        clearMessages();

        String floorText = floorField.getText().trim();
        String selectedType = roomTypeCombo.getValue();

        boolean valid = true;

        if (floorText.isEmpty()) {
            floorError.setText("Floor is required.");
            valid = false;
        }

        int floor = 0;
        if (valid) {
            try {
                floor = Integer.parseInt(floorText);
            } catch (NumberFormatException e) {
                floorError.setText("Enter a valid floor number.");
                valid = false;
            }
        }

        if (selectedType == null || selectedType.isEmpty()) {
            roomTypeError.setText("Select a room type.");
            valid = false;
        }

        if (!valid) return;

        try {
            Database.getAdmin().createRoom(floor, selectedType);
            addRoomMessage.setStyle("-fx-text-fill: #768064;");
            addRoomMessage.setText("✓  Room successfully added.");
            floorField.clear();
            roomTypeCombo.getSelectionModel().clearSelection();
            refreshRoomList();
        } catch (Exception e) {
            addRoomMessage.setText("Error: " + e.getMessage());
        }
    }


    @FXML
    private void handleUpdateStatus(ActionEvent event) {
        clearMessages();

        String numText = updateRoomNumberField.getText().trim();
        Rooms.RoomStatus newStatus = statusCombo.getValue();

        if (numText.isEmpty() || newStatus == null) {
            updateStatusMessage.setText("Room number and status are required.");
            return;
        }

        try {
            int roomNumber = Integer.parseInt(numText);
            Database.getAdmin().updateAvailability(newStatus, roomNumber);
            updateStatusMessage.setStyle("-fx-text-fill: #768064;");
            updateStatusMessage.setText("✓  Room " + roomNumber + " status updated to " + newStatus + ".");
            updateRoomNumberField.clear();
            refreshRoomList();
        } catch (NumberFormatException e) {
            updateStatusMessage.setText("Enter a valid room number.");
        } catch (Exception e) {
            updateStatusMessage.setText("Error: " + e.getMessage());
        }
    }



    @FXML
    private void handleDeleteRoom(ActionEvent event) {
        clearMessages();

        String numText = deleteRoomNumberField.getText().trim();

        if (numText.isEmpty()) {
            deleteRoomMessage.setText("Room number is required.");
            return;
        }

        try {
            int roomNumber = Integer.parseInt(numText);
            Database.getAdmin().deleteRoom(roomNumber);
            deleteRoomMessage.setStyle("-fx-text-fill: #768064;");
            deleteRoomMessage.setText("✓  Room " + roomNumber + " removed.");
            deleteRoomNumberField.clear();
            refreshRoomList();
        } catch (NumberFormatException e) {
            deleteRoomMessage.setText("Enter a valid room number.");
        } catch (Exception e) {
            deleteRoomMessage.setText("Error: " + e.getMessage());
        }
    }



    private void refreshRoomTypeCombo() {
        roomTypeCombo.getItems().clear();
        for (RoomType rt : Database.getAvailableRoomTypesList()) {
            roomTypeCombo.getItems().add(rt.getTypeName());
        }
    }

    private void refreshRoomList() {

        roomsContainer.getChildren().clear();
        int count = Database.getRoomList().size();
        roomCountLabel.setText(count + (count == 1 ? " ROOM" : " ROOMS"));

        for (Rooms room : Database.getRoomList()) {

            HBox row = new HBox();
            row.getStyleClass().add("activity-item");
            row.setPadding(new Insets(14, 16, 14, 16));
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            String typeName = (room.getRoomtype() != null) ? room.getRoomtype().getTypeName() : "—";
            String amenityCount = room.getAmenities().size() + " attached";
            boolean available = room.getStatus() == Rooms.RoomStatus.AVAILABLE;

            Label numLbl   = styledLabel("# " + room.getRoomNumber(), 100, "#DADED8");
            Label floorLbl = styledLabel("Floor " + room.getRoomFloor(), 100, "#959581");
            Label typeLbl  = styledLabel(typeName, 200, "#DADED8");
            Label statusLbl = styledLabel(
                    available ? "AVAILABLE" : "OCCUPIED", 150,
                    available ? "#768064" : "#8B6060"
            );
            Label amenityLbl = styledLabel(amenityCount, 120, "#959581");

            row.getChildren().addAll(numLbl, floorLbl, typeLbl, statusLbl, amenityLbl);
            roomsContainer.getChildren().add(row);
        }

        if (count == 0) {
            Label empty = new Label("No rooms yet. Add one above.");
            empty.getStyleClass().add("explanation-text");
            empty.setPadding(new Insets(20, 16, 20, 16));
            roomsContainer.getChildren().add(empty);
        }
    }

    private Label styledLabel(String text, double width, String color) {
        Label lbl = new Label(text);
        lbl.setPrefWidth(width);
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-family: 'Montserrat Light'; -fx-font-size: 12px;");
        return lbl;
    }

    private void clearMessages() {
        floorError.setText("");
        roomTypeError.setText("");
        addRoomMessage.setText("");
        updateStatusMessage.setText("");
        deleteRoomMessage.setText("");
    }
}