import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RoomStatusDialogController {

    @FXML private Label roomLabel;
    @FXML private Label statusPreview;

    private Rooms room;
    private Runnable onConfirm;
    private Rooms.RoomStatus selectedStatus;
    private ManageRoomsController parentController;

    public void setData(Rooms room, Runnable onConfirm) {
        this.room = room;
        this.onConfirm = onConfirm;

        roomLabel.setText("Room " + room.getRoomNumber());
        selectedStatus = room.getStatus();
        updatePreview(selectedStatus);
    }

    // Called from ManageRoomsController so cancel can close the overlay
    public void setParentController(ManageRoomsController parentController) {
        this.parentController = parentController;
    }

    private void updatePreview(Rooms.RoomStatus status) {
        statusPreview.setText(status.toString());
    }

    @FXML
    private void setAvailable() {
        selectedStatus = Rooms.RoomStatus.AVAILABLE;
        updatePreview(selectedStatus);
    }

    @FXML
    private void setOccupied() {
        selectedStatus = Rooms.RoomStatus.OCCUPIED;
        updatePreview(selectedStatus);
    }

    @FXML
    private void confirm() {
        room.setStatus(selectedStatus);

        Database.addActivity(
                "Room " + room.getRoomNumber() + " updated to " + selectedStatus
        );

        if (onConfirm != null) onConfirm.run();
    }

    @FXML
    private void cancel() {
        if (parentController != null) parentController.hideOverlay();
    }
}
