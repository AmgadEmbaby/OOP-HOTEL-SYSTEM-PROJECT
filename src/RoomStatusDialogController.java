import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RoomStatusDialogController {

    @FXML private Label roomLabel;
    @FXML private Button availableBtn;
    @FXML private Button occupiedBtn;

    private Rooms room;
    private Runnable onConfirm;
    private Rooms.RoomStatus selectedStatus;
    private ManageRoomsController parentController;

    public void setData(Rooms room, Runnable onConfirm) {
        this.room = room;
        this.onConfirm = onConfirm;
        roomLabel.setText("Room " + room.getRoomNumber());
        selectedStatus = room.getStatus();
        updateButtons(selectedStatus);
    }

    public void setParentController(ManageRoomsController parentController) {
        this.parentController = parentController;
    }

    private void updateButtons(Rooms.RoomStatus status) {
        availableBtn.getStyleClass().removeAll("minimal-button-selected");
        occupiedBtn.getStyleClass().removeAll("minimal-button-selected");

        if (status == Rooms.RoomStatus.AVAILABLE) {
            availableBtn.getStyleClass().add("minimal-button-selected");
        } else {
            occupiedBtn.getStyleClass().add("minimal-button-selected");
        }
    }

    @FXML
    private void setAvailable() {
        selectedStatus = Rooms.RoomStatus.AVAILABLE;
        updateButtons(selectedStatus);
    }

    @FXML
    private void setOccupied() {
        selectedStatus = Rooms.RoomStatus.OCCUPIED;
        updateButtons(selectedStatus);
    }

    @FXML
    private void confirm() {
        room.setStatus(selectedStatus);
        Database.addActivity("Room " + room.getRoomNumber() + " updated to " + selectedStatus);
        if (onConfirm != null) onConfirm.run();
    }

    @FXML
    private void cancel() {
        if (parentController != null) parentController.hideOverlay();
    }
}