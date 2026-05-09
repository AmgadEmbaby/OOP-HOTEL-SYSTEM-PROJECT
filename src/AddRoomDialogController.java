import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddRoomDialogController {

    @FXML private TextField floorField;
    @FXML private ComboBox<String> roomTypeCombo;
    @FXML private Label dialogErrorLabel;

    private ManageRoomsController parent;

    public void setParent(ManageRoomsController parent) {
        this.parent = parent;

        roomTypeCombo.getItems().setAll(
                Database.getAvailableRoomTypesList()
                        .stream()
                        .map(RoomType::getTypeName)
                        .toList()
        );
    }

    @FXML
    private void createRoom() {

        try {
            String floor = floorField.getText();
            String type = roomTypeCombo.getValue();

            if (floor.isEmpty() || type == null) {
                dialogErrorLabel.setText("Fill all fields");
                return;
            }

            parent.createRoomFromDialog(floor, type);

        } catch (Exception e) {
            dialogErrorLabel.setText("Invalid input");
        }
    }

    @FXML
    private void closeDialog() {
        parent.hideOverlay();
    }
}