import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RoomStatusDialogController {

    @FXML private Label roomLabel;
    @FXML private Label statusPreview;

    private Rooms room;
    private Runnable onConfirm;

    public void setData(Rooms room, Runnable onConfirm) {
        this.room = room;
        this.onConfirm = onConfirm;

        roomLabel.setText("Room " + room.getRoomNumber());
        updatePreview(room.getStatus());
    }

    private void updatePreview(Rooms.RoomStatus status) {
        statusPreview.setText(status.toString());
    }

    @FXML
    private void setAvailable() {
        room.setStatus(Rooms.RoomStatus.AVAILABLE);
        updatePreview(Rooms.RoomStatus.AVAILABLE);
    }

    @FXML
    private void setOccupied() {
        room.setStatus(Rooms.RoomStatus.OCCUPIED);
        updatePreview(Rooms.RoomStatus.OCCUPIED);
    }

    @FXML
    private void confirm() {
        if (onConfirm != null) onConfirm.run();
    }

    @FXML
    private void cancel() {
        close();
    }

    private void close() {
        //roomLabel.getScene().getWindow().hide();
    }
}